package com.ikiwq.blog.api.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BlogExceptionEnum {
    // User
    USER_USERNAME_ALREADY_TAKEN(400, "A user with this username already exists"),

    // Article
    ARTICLE_SLUG_ALREADY_TAKEN(400, "An article with this slug already exists"),
    ARTICLE_NOT_FOUND(404, "Article not found"),

    // Category
    CATEGORY_NOT_FOUND(404, "Category not found"),

    // Files
    FILE_EMPTY(400, "File is empty"),
    FILE_INVALID_NAME(400, "File name is invalid"),
    FILE_INVALID_DIRECTORY(400, "Invalid file directory"),
    FILE_UPLOAD_FAILED(500, "File upload has failed"),
    FILE_NOT_FOUND(404, "File not found");

    private final int httpStatusCode;
    private final String message;
}
