package com.ikiwq.blog.api.config.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AuthenticationExceptionHandler {
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<Map<String, String>> defaultErrorHandler(HttpServletRequest req, AuthenticationException e){
        Map<String, String> res = new HashMap<>();
        res.put("message", e.getMessage());
        res.put("path", req.getServletPath());

        return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
    }
}
