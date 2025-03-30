package com.ikiwq.blog.api.config.handler;

import com.ikiwq.blog.api.model.exception.BlogException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = BlogException.class)
    public ResponseEntity<Map<String, String>> defaultErrorHandler(HttpServletRequest req, BlogException e){
        Map<String, String> res = e.getAdditionalInfo();
        res.put("message", e.getMessage());

        String path = String.format("%s %s", req.getMethod(), req.getServletPath());
        res.put("path", path);

        return new ResponseEntity<>(res, e.getHttpStatusCode());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleMessageNotReadableExceptions(
            HttpServletRequest req,
            HttpMessageNotReadableException e
    ) {
        String path = String.format("%s %s", req.getMethod(), req.getServletPath());
        Map<String, String> res = Map.of(
                "message", "Body is required for this method",
                "path", path
        );
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            HttpServletRequest req,
            MethodArgumentNotValidException e
    ) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String path = String.format("%s %s", req.getMethod(), req.getServletPath());
        Map<String, Object> res = Map.of(
                "message", "Some required fields are missing",
                "errors", errors,
                "path", path
        );
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}
