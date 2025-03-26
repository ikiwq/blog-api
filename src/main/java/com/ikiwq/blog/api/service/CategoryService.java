package com.ikiwq.blog.api.service;

import com.ikiwq.blog.api.model.dto.response.CategoryResponse;
import com.ikiwq.blog.api.model.dto.request.CategoryCreationRequest;
import com.ikiwq.blog.api.model.entity.Category;
import com.ikiwq.blog.api.model.exception.BlogException;
import com.ikiwq.blog.api.model.exception.BlogExceptionEnum;
import com.ikiwq.blog.api.model.mapper.CategoryMapper;
import com.ikiwq.blog.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryResponse getCategoryBySlug(String categorySlug) {
        Category category = categoryRepository.findBySlug(categorySlug)
                .orElseThrow(() -> new BlogException(BlogExceptionEnum.CATEGORY_NOT_FOUND));
        return categoryMapper.toResponse(category);
    }

    public Category getCategoryByIdRaw(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BlogException(BlogExceptionEnum.CATEGORY_NOT_FOUND));
    }

    public List<CategoryResponse> getCategories(int page, int take) {
        Pageable pageable = PageRequest.of(page, take);
        Page<Category> categories = categoryRepository.findAll(pageable);

        return categories.stream().map(categoryMapper::toResponse).toList();
    }

    public CategoryResponse createCategory(CategoryCreationRequest request) {
        Category category = categoryMapper.toEntity(request);

        return categoryMapper.toResponse(categoryRepository.save(category));
    }
}
