package com.ikiwq.blog.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticlePayloadRequest {
    @NotBlank(message = "Title is mandatory")
    private String title;
    private String excerpt;
    private String content;
    private String image;

    @NotBlank(message = "Slug is mandatory")
    private String slug;

    private boolean pinned;

    @NotNull(message = "Category id is mandatory")
    private long categoryId;
}
