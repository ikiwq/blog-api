package com.ikiwq.blog.api.controller;

import com.ikiwq.blog.api.model.dto.response.CategoryResponse;
import com.ikiwq.blog.api.model.dto.request.CategoryCreationRequest;
import com.ikiwq.blog.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{categorySlug}")
    public CategoryResponse getCategory(@PathVariable String categorySlug) {
        return categoryService.getCategoryBySlug(categorySlug);
    }

    @GetMapping("/")
    public List<CategoryResponse> getCategories(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int take
    ) {
        return categoryService.getCategories(page, take);
    }

    @PostMapping("/")
    public CategoryResponse createCategory(CategoryCreationRequest request) {
        return categoryService.createCategory(request);
    }

}
