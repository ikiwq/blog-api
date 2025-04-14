package com.ikiwq.blog.api.controller;

import com.ikiwq.blog.api.model.dto.request.CategoryPayloadRequest;
import com.ikiwq.blog.api.model.dto.response.CategoryResponse;
import com.ikiwq.blog.api.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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

    @Operation(summary = "Get category by slug", description = "Fetch a single category using its unique slug identifier.")
    @GetMapping("/{categorySlug}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable String categorySlug) {
        CategoryResponse res = categoryService.getCategoryBySlug(categorySlug);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "Get all categories", description = "Retrieve a list of categories.")
    @GetMapping("/")
    public ResponseEntity<List<CategoryResponse>> getCategories(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int take
    ) {
        List<CategoryResponse> res = categoryService.getCategories(page, take);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "Create a new category", description = "Submit a new category with a title and other optional fields.")
    @PostMapping("/")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryPayloadRequest request) {
        CategoryResponse res = categoryService.createCategory(request);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing category", description = "Replace an existing category's content by ID.")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> putCategory(
            @PathVariable long categoryId,
            @RequestBody @Valid CategoryPayloadRequest request
    ) {
        CategoryResponse res = categoryService.putCategory(categoryId, request);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
