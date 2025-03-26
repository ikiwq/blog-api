package com.ikiwq.blog.api.model.mapper;

import com.ikiwq.blog.api.model.dto.request.ArticlePayloadRequest;
import com.ikiwq.blog.api.model.dto.response.ArticleResponse;
import com.ikiwq.blog.api.model.entity.Article;
import org.mapstruct.*;

import java.time.Instant;

@Mapper(componentModel = "spring", imports = {Instant.class})
public interface ArticleMapper {
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "pinned", defaultValue = "false"),
        @Mapping(target = "author", ignore = true),
        @Mapping(target = "category", ignore = true),
        @Mapping(target = "createdAt", expression = "java(Instant.now())")
    })
    Article toEntity(ArticlePayloadRequest request);

    @Mappings({
            @Mapping(target = "readingTimeSeconds", ignore = true)
    })
    ArticleResponse toResponse(Article article);

    @AfterMapping
    default void afterMappingToResponse(@MappingTarget ArticleResponse articleResponse){
        // Divide by an estimation of 6 characters per word. Splitting by space is expensive!
        int wordCount = articleResponse.getContent().length() / 6;
        // Divide by an average of 200 words per minute, or 12.5 words per second
        articleResponse.setReadingTimeSeconds((int) Math.round(wordCount / 12.5));

        articleResponse.setCreatedAt(Instant.now());
    }

    @Mappings({
            @Mapping(target = "id", source = "article.id"),
            @Mapping(target = "title", source = "request.title"),
            @Mapping(target = "excerpt", source = "request.excerpt"),
            @Mapping(target = "content", source = "request.content"),
            @Mapping(target = "slug", source = "request.slug"),
            @Mapping(target = "image", source = "request.image"),
            @Mapping(target = "pinned", source = "request.pinned"),
            @Mapping(target = "author", source = "article.author"),
            @Mapping(target = "category", source = "article.category"),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "publishedAt", source = "request.publishedAt")
    })
    Article merge(Article article, ArticlePayloadRequest request);
}
