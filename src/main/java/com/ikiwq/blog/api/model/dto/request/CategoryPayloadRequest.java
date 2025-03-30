package com.ikiwq.blog.api.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryPayloadRequest {
    private String name;
    private String description;

    private String slug;
    private String image;
}
