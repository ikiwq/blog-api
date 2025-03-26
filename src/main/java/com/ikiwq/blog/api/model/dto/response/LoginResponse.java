package com.ikiwq.blog.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String authToken;
    private Instant authTokenExpiresAt;

    private String refreshToken;
    private Instant refreshTokenExpiresAt;
}
