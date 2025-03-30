package com.ikiwq.blog.api.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class CategoryResponse {
    private long id;

    private String name;
    private String description;

    private String slug;
    private String image;

    private Instant createdAt;
}
