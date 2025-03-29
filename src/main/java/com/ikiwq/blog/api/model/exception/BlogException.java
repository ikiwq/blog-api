package com.ikiwq.blog.api.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BlogException extends RuntimeException {
    private final HttpStatusCode httpStatusCode;
    private final String message;
    private final Map<String, String> additionalInfo = new HashMap<>();

    public BlogException(AuthExceptionEnum exceptionEnum) {
        this.httpStatusCode = HttpStatusCode.valueOf(exceptionEnum.getHttpStatusCode());
        this.message = exceptionEnum.getMessage();
    }

    public BlogException(BlogExceptionEnum exceptionEnum) {
        this.httpStatusCode = HttpStatusCode.valueOf(exceptionEnum.getHttpStatusCode());
        this.message = exceptionEnum.getMessage();
    }

    public BlogException(BlogExceptionEnum exceptionEnum, Map<String, String> additionalInfo) {
        this(exceptionEnum);
        this.additionalInfo.putAll(additionalInfo);
    }
}
