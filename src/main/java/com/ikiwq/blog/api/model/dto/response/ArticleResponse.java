package com.ikiwq.blog.api.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ArticleResponse {
    private long id;

    private String slug;
    private String title;
    private String excerpt;
    private String content;

    private String image;
    private int readingTimeSeconds;

    private boolean pinned;

    private Instant createdAt;
}
