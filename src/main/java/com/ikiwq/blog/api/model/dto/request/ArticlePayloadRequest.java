package com.ikiwq.blog.api.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticlePayloadRequest {
    private String title;
    private String excerpt;
    private String content;

    private String image;

    private String slug;
    private boolean pinned;

    private long categoryId;
}
