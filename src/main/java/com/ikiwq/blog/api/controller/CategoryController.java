package com.ikiwq.blog.api.controller;

import com.ikiwq.blog.api.model.dto.response.CategoryResponse;
import com.ikiwq.blog.api.model.dto.request.CategoryCreationRequest;
import com.ikiwq.blog.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{categorySlug}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable String categorySlug) {
        CategoryResponse res = categoryService.getCategoryBySlug(categorySlug);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryResponse>> getCategories(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int take
    ) {
        List<CategoryResponse> res = categoryService.getCategories(page, take);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<CategoryResponse> createCategory(CategoryCreationRequest request) {
        CategoryResponse res = categoryService.createCategory(request);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
