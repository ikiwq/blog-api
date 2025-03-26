package com.ikiwq.blog.api.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BlogExceptionEnum {
    USER_USERNAME_ALREADY_TAKEN(400, "A user with this username already exists"),
    ARTICLE_SLUG_ALREADY_TAKEN(400, "An article with this slug already exists"),
    ARTICLE_NOT_FOUND(404, "Article not found"),
    CATEGORY_NOT_FOUND(404, "Category not found"),
    REFRESH_TOKEN_INVALID(403, "Refresh token is invalid");

    private final int httpStatusCode;
    private final String message;
}
