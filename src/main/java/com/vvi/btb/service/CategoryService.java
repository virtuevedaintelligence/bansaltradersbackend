package com.vvi.btb.service;

import com.vvi.btb.domain.response.CategoryRequest;
import com.vvi.btb.domain.response.CategoryResponse;
import com.vvi.btb.exception.category.CategoryException;

import java.util.List;

public interface CategoryService {

    CategoryResponse saveCategory(CategoryRequest categoryRequest) throws CategoryException;

    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) throws CategoryException;

    boolean deleteCategory(Long id) throws CategoryException;

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryByName(String categoryName) throws CategoryException;
}