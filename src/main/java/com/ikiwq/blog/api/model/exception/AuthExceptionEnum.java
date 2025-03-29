package com.ikiwq.blog.api.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthExceptionEnum {

    USERNAME_PASSWORD_INVALID(401, "Invalid username or password"),
    AUTH_TOKEN_INVALID(401, "Invalid authentication token"),
    REFRESH_TOKEN_INVALID(401, "Invalid refresh token"),
    AUTH_METHOD_NOT_SUPPORTED(401, "Auth method is not supported");

    private final int httpStatusCode;
    private final String message;
}
