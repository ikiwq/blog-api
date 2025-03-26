package com.ikiwq.blog.api.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ArticlePayloadRequest {
    private String title;
    private String excerpt;
    private String content;

    private String slug;
    private boolean pinned;

    private long categoryId;

    private Instant publishedAt;
}
