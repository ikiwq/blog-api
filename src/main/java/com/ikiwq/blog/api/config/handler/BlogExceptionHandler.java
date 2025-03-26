package com.ikiwq.blog.api.config.handler;

import com.ikiwq.blog.api.model.exception.BlogException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class BlogExceptionHandler {
    @ExceptionHandler(value = BlogException.class)
    public ResponseEntity<Map<String, String>> defaultErrorHandler(HttpServletRequest req, BlogException e){
        Map<String, String> res = e.getAdditionalInfo();
        res.put("message", e.getMessage());
        res.put("path", req.getServletPath());

        return new ResponseEntity<>(res, e.getHttpStatusCode());
    }
}
