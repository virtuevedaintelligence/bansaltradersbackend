package com.vvi.btb.controller;


import com.vvi.btb.domain.HttpResponse;
import com.vvi.btb.domain.request.CategoryRequest;
import com.vvi.btb.domain.response.CategoryResponse;
import com.vvi.btb.exception.domain.CategoryException;
import com.vvi.btb.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.vvi.btb.constant.CategoryImplConstant.CATEGORY_DELETED_SUCCESSFULLY;
import static com.vvi.btb.constant.CategoryImplConstant.PLEASE_CONTACT_ADMIN;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1/categories")
public class CategoryResource {
    private CategoryService categoryService;
    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/createCategory")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) throws CategoryException {
        CategoryResponse category = categoryService.getCategoryByName(categoryRequest.getCategoryName());
        if(category != null){
            return new ResponseEntity<>(category, HttpStatus.CONFLICT);
        }
        CategoryResponse categoryResponse = categoryService.saveCategory(categoryRequest);
        return new ResponseEntity<>(categoryResponse, OK);
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable("id") long id, @RequestBody CategoryRequest category) throws CategoryException {
        CategoryResponse categoryResponse = categoryService.updateCategory(id, category);
        if(categoryResponse == null){
            return new ResponseEntity<>(new HttpResponse(400, HttpStatus.BAD_REQUEST,PLEASE_CONTACT_ADMIN,"NA"),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(categoryResponse, OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<HttpResponse> deleteCategory(@PathVariable("categoryId") Long categoryId) throws CategoryException {
        categoryService.deleteCategory(categoryId);
        return response(OK, CATEGORY_DELETED_SUCCESSFULLY);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
            return new ResponseEntity<>(categoryService.getAllCategories(),OK);
    }
    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
