package com.ikiwq.blog.api.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class GeneratedToken {
    private String token;
    private Instant expiresAt;
}
