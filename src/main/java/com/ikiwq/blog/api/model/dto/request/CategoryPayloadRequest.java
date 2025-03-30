package com.ikiwq.blog.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryPayloadRequest {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private String image;

    @NotBlank(message = "Slug is required")
    private String slug;
}
