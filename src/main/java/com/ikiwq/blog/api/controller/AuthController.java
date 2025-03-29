package com.ikiwq.blog.api.controller;

import com.ikiwq.blog.api.model.dto.request.LoginRequest;
import com.ikiwq.blog.api.model.dto.request.RefreshRequest;
import com.ikiwq.blog.api.model.dto.response.LoginResponse;
import com.ikiwq.blog.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginResponse(@RequestBody LoginRequest request) {
        LoginResponse res = authService.login(request);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshRequest request) {
        LoginResponse res = authService.refresh(request);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
