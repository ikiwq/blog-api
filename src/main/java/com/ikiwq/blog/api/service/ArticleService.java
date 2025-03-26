package com.ikiwq.blog.api.service;

import com.ikiwq.blog.api.model.dto.request.ArticlePayloadRequest;
import com.ikiwq.blog.api.model.dto.response.ArticleResponse;
import com.ikiwq.blog.api.model.entity.Article;
import com.ikiwq.blog.api.model.entity.Category;
import com.ikiwq.blog.api.model.entity.specification.ArticleSpecifications;
import com.ikiwq.blog.api.model.exception.BlogException;
import com.ikiwq.blog.api.model.exception.BlogExceptionEnum;
import com.ikiwq.blog.api.model.mapper.ArticleMapper;
import com.ikiwq.blog.api.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final CategoryService categoryService;

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    public ArticleResponse getArticleBySlug(String articleSlug){
        Article article = articleRepository.findBySlug(articleSlug)
                .orElseThrow(() -> new BlogException(BlogExceptionEnum.ARTICLE_NOT_FOUND));

        return articleMapper.toResponse(article);
    }

    public List<ArticleResponse> getArticles(int page, int take, String category, Boolean pinned) {
        Pageable pageable = PageRequest.of(page, take);
        Specification<Article> spec = ArticleSpecifications.combinedSpecifications(category, pinned);

        Page<Article> articles = articleRepository.findAll(spec, pageable);

        return articles.stream().map(articleMapper::toResponse).toList();
    }

    public ArticleResponse createArticle(ArticlePayloadRequest request) {
        if(articleRepository.findBySlug(request.getSlug()).isPresent()){
            throw new BlogException(BlogExceptionEnum.ARTICLE_SLUG_ALREADY_TAKEN);
        }

        Category category = categoryService.getCategoryByIdRaw(request.getCategoryId());

        Article article = articleMapper.toEntity(request);
        article.setCategory(category);

        return articleMapper.toResponse(articleRepository.save(article));
    }

    public ArticleResponse putArticle(long articleId, ArticlePayloadRequest request) {
        Article originalArticle = articleRepository.findById(articleId)
                .orElseThrow(() ->  new BlogException(BlogExceptionEnum.ARTICLE_NOT_FOUND));

        if(articleRepository.findBySlug(request.getSlug()).isPresent()){
            throw new BlogException(BlogExceptionEnum.ARTICLE_SLUG_ALREADY_TAKEN);
        }

        Article putArticle = articleMapper.merge(originalArticle, request);
        return articleMapper.toResponse(articleRepository.save(putArticle));
    }
}
