package com.ikiwq.blog.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshRequest {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
