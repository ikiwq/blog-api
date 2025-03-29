package com.ikiwq.blog.api.controller;

import com.ikiwq.blog.api.model.dto.request.UserPayloadRequest;
import com.ikiwq.blog.api.model.dto.response.UserResponse;
import com.ikiwq.blog.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserPayloadRequest creationRequest) {
        UserResponse res = userService.createUser(creationRequest);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
