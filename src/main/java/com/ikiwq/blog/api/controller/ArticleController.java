package com.ikiwq.blog.api.controller;

import com.ikiwq.blog.api.model.dto.request.ArticlePayloadRequest;
import com.ikiwq.blog.api.model.dto.response.ArticleResponse;
import com.ikiwq.blog.api.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @Operation(summary = "Get article by slug", description = "Fetch a single article using its unique slug identifier.")
    @GetMapping("/{articleSlug}")
    public ResponseEntity<ArticleResponse> getArticle(
            @PathVariable String articleSlug
    ) {
        ArticleResponse res = articleService.getArticleBySlug(articleSlug);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "Get all articles", description = "Retrieve a list of articles with optional filters like category or pinned status.")
    @GetMapping("/")
    public ResponseEntity<List<ArticleResponse>> getArticles(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int take,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean pinned
    ) {
        List<ArticleResponse> res = articleService.getArticles(page, take, category, pinned);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "Create a new article", description = "Submit a new article with a title, content, category, and other optional fields.")
    @PostMapping("/")
    public ResponseEntity<ArticleResponse> createArticle(
            @RequestBody @Valid ArticlePayloadRequest request
    ) {
        ArticleResponse res = articleService.createArticle(request);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing article", description = "Replace an existing article's content by ID.")
    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> putArticle(
            @PathVariable long articleId,
            @RequestBody @Valid ArticlePayloadRequest request
    ) {
        ArticleResponse res = articleService.putArticle(articleId, request);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
