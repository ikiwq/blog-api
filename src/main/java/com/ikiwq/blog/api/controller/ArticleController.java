package com.ikiwq.blog.api.controller;

import com.ikiwq.blog.api.model.dto.request.ArticlePayloadRequest;
import com.ikiwq.blog.api.model.dto.response.ArticleResponse;
import com.ikiwq.blog.api.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/{articleSlug}")
    public ArticleResponse getArticle(@PathVariable String articleSlug){
        return articleService.getArticleBySlug(articleSlug);
    }

    @GetMapping("/")
    public List<ArticleResponse> getArticles(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int take,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean pinned
    ) {
        return articleService.getArticles(page, take, category, pinned);
    }

    @PostMapping("/")
    public ArticleResponse createArticle(ArticlePayloadRequest request) {
        return articleService.createArticle(request);
    }

    @PutMapping("/{articleId}")
    public ArticleResponse putArticle(
            @PathVariable long articleId,
            @RequestBody ArticlePayloadRequest request
    ) {
        return articleService.putArticle(articleId, request);
    }
}
