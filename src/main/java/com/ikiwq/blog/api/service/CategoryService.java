package com.ikiwq.blog.api.service;

import com.ikiwq.blog.api.model.dto.response.CategoryResponse;
import com.ikiwq.blog.api.model.dto.request.CategoryPayloadRequest;
import com.ikiwq.blog.api.model.entity.Category;
import com.ikiwq.blog.api.model.exception.BlogException;
import com.ikiwq.blog.api.model.exception.BlogExceptionEnum;
import com.ikiwq.blog.api.model.mapper.CategoryMapper;
import com.ikiwq.blog.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CacheManager cacheManager;

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Cacheable(value = "category", key = "#categorySlug")
    public CategoryResponse getCategoryBySlug(String categorySlug) {
        Category category = categoryRepository.findBySlug(categorySlug)
                .orElseThrow(() -> new BlogException(BlogExceptionEnum.CATEGORY_NOT_FOUND));
        return categoryMapper.toResponse(category);
    }

    public Category getCategoryByIdRaw(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BlogException(BlogExceptionEnum.CATEGORY_NOT_FOUND));
    }

    @Cacheable(value = "categories", key = "{#page, #take}")
    public List<CategoryResponse> getCategories(int page, int take) {
        Pageable pageable = PageRequest.of(page, take);
        Page<Category> categories = categoryRepository.findAll(pageable);

        return categories.stream().map(categoryMapper::toResponse).toList();
    }

    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse createCategory(CategoryPayloadRequest request) {
        Category category = categoryMapper.toEntity(request);

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse putCategory(long categoryId, CategoryPayloadRequest request) {
        Category originalCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BlogException(BlogExceptionEnum.CATEGORY_NOT_FOUND));

        if(categoryRepository.findBySlug(request.getSlug()).isPresent()) {
            throw new BlogException(BlogExceptionEnum.CATEGORY_SLUG_ALREADY_TAKEN);
        }

        Category putCategory = categoryMapper.merge(originalCategory, request);
        CategoryResponse res = categoryMapper.toResponse(categoryRepository.save(putCategory));

        evictCategoryCacheBySlug(originalCategory.getSlug());
        return res;
    }

    private void evictCategoryCacheBySlug(String slug){
        try {
            Objects.requireNonNull(
                    cacheManager.getCache("category")
            ).evict(slug);
        } catch (NullPointerException e) {
            String message = String.format(
                    "Tried to evict category cache with slug %s, but an exception occurred:",
                    slug
            );
            log.error(message, e);
        }
    }
}
