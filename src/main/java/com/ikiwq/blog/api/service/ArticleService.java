package com.ikiwq.blog.api.service;

import com.ikiwq.blog.api.model.dto.request.ArticlePayloadRequest;
import com.ikiwq.blog.api.model.dto.response.ArticleResponse;
import com.ikiwq.blog.api.model.entity.Article;
import com.ikiwq.blog.api.model.entity.BlogUser;
import com.ikiwq.blog.api.model.entity.Category;
import com.ikiwq.blog.api.model.entity.specification.ArticleSpecifications;
import com.ikiwq.blog.api.model.exception.BlogException;
import com.ikiwq.blog.api.model.exception.BlogExceptionEnum;
import com.ikiwq.blog.api.model.mapper.ArticleMapper;
import com.ikiwq.blog.api.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.ikiwq.blog.api.util.AuthenticationUtils.getCurrentUserId;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final CacheManager cacheManager;

    private final CategoryService categoryService;

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    @Cacheable(value = "article", key = "#articleSlug")
    public ArticleResponse getArticleBySlug(String articleSlug){
        Article article = articleRepository.findBySlug(articleSlug)
                .orElseThrow(() -> new BlogException(BlogExceptionEnum.ARTICLE_NOT_FOUND));

        return articleMapper.toResponse(article);
    }

    @Cacheable(value = "articles", key = "{#page, #take, #category, #pinned}")
    public List<ArticleResponse> getArticles(int page, int take, String category, Boolean pinned) {
        Pageable pageable = PageRequest.of(page, take);
        Specification<Article> spec = ArticleSpecifications.combinedSpecifications(category, pinned);

        Page<Article> articles = articleRepository.findAll(spec, pageable);

        return articles.stream().map(articleMapper::toResponse).toList();
    }

    @CacheEvict(value = "articles", allEntries = true)
    public ArticleResponse createArticle(ArticlePayloadRequest request) {
        if(articleRepository.findBySlug(request.getSlug()).isPresent()){
            throw new BlogException(BlogExceptionEnum.ARTICLE_SLUG_ALREADY_TAKEN);
        }

        Category category = categoryService.getCategoryByIdRaw(request.getCategoryId());

        Article article = articleMapper.toEntity(request);
        article.setCategory(category);
        article.setAuthor(new BlogUser(getCurrentUserId()));

        return articleMapper.toResponse(articleRepository.save(article));
    }

    @CacheEvict(value = "articles", allEntries = true)
    public ArticleResponse putArticle(long articleId, ArticlePayloadRequest request) {
        Article originalArticle = articleRepository.findById(articleId)
                .orElseThrow(() ->  new BlogException(BlogExceptionEnum.ARTICLE_NOT_FOUND));

        if(getCurrentUserId() != originalArticle.getAuthor().getId()) {
            throw new BlogException(BlogExceptionEnum.ARTICLE_NOT_CREATED_BY_USER);
        }

        if(articleRepository.findBySlug(request.getSlug()).isPresent()){
            throw new BlogException(BlogExceptionEnum.ARTICLE_SLUG_ALREADY_TAKEN);
        }

        Article putArticle = articleMapper.merge(originalArticle, request);

        Category articleCategory = categoryService.getCategoryByIdRaw(request.getCategoryId());
        putArticle.setCategory(articleCategory);

        ArticleResponse res = articleMapper.toResponse(articleRepository.save(putArticle));

        evictArticleCacheBySlug(originalArticle.getSlug());
        return res;
    }

    private void evictArticleCacheBySlug(String slug){
        try {
            Objects.requireNonNull(
                    cacheManager.getCache("article")
            ).evict(slug);
        } catch (NullPointerException e) {
            String message = String.format(
                    "Tried to evict article cache with slug %s, but an exception occurred:",
                    slug
            );
           log.error(message, e);
        }
    }
}
