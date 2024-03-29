package com.vvi.btb.service.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import com.vvi.btb.domain.request.user.UserOTPRequest;
import com.vvi.btb.domain.response.OTPResponse;
import com.vvi.btb.service.abs.OTPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.vvi.btb.constant.UserImplConstant.*;

@Service
@Slf4j
public record OTPServiceImpl() implements OTPService {
    private static Map<Long, OTPResponse> cache = new ConcurrentHashMap<>();

    @Override
    public Optional<OTPResponse> sendOTP(long number) {
        log.info("in send OTP");
        OTPResponse success = new OTPResponse();
        int count = 1;
        if(cache.containsKey(number)) {
             success = cache.get(number);
             count = count+cache.get(number).getCount();
             if(count <= 3){
                 success = getOtpResponse(success, count,number);
                 cache.put(number, success);
             }else if(LocalDateTime.now().isAfter(cache.get(number).getTime().plusMinutes(3))){
                 success = getOtpResponse(success, 1,number);
             }
        }else{
            success = getOtpResponse(success, count, number);
            cache.put(number, success);
        }
        log.info(" " + success.getOtp());
        return Optional.ofNullable(success);
    }

    private OTPResponse getOtpResponse(OTPResponse otpResponse, int count, long number) {
        // nexon code
        // vonageApi(otpResponse, number);
        otpResponse.setStatus(SUCCESS);
        otpResponse.setOtp(generateOTP());
        otpResponse.setCount(count);
        otpResponse.setTime(LocalDateTime.now());
        return otpResponse;
    }

    private void vonageApi(OTPResponse otpResponse, long number) {

        VonageClient client = VonageClient.builder().apiKey("f6fd3928").apiSecret("6YHCb8wRvOj6cHcn").build();
        String NUMBER = String.valueOf(number);
        TextMessage message = new TextMessage(FROM, COUNTRY_CODE + NUMBER, MESSAGE_BODY);
        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            otpResponse.setStatus(SUCCESS);
        } else {
            otpResponse.setStatus(FAILED);
        }
    }

    @Override
    public boolean verifyOTP(UserOTPRequest userOTPRequest) {
        boolean validated = false;
        if(cache.containsKey(Long.parseLong(userOTPRequest.getNumber()))){
             validated = Long.parseLong(userOTPRequest.getOtp()) == Long.parseLong(cache.get(Long.parseLong(userOTPRequest.getNumber())).getOtp());
             cache.remove(Long.parseLong(userOTPRequest.getNumber()));
        }
        return validated;
    }

    private String generateOTP(){
        return new DecimalFormat("10000")
                .format(new Random().nextInt(99999));
    }
}
