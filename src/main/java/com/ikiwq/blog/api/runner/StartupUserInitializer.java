package com.ikiwq.blog.api.runner;

import com.ikiwq.blog.api.model.dto.request.UserPayloadRequest;
import com.ikiwq.blog.api.model.entity.BlogUser;
import com.ikiwq.blog.api.model.enumeration.RoleEnum;
import com.ikiwq.blog.api.repository.UserRepository;
import com.ikiwq.blog.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StartupUserInitializer implements CommandLineRunner {
    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${app.admin.credentials.username}")
    private String username;

    @Value("${app.admin.credentials.password}")
    private String password;

    @Value("${app.admin.credentials.image:#{null}}")
    private String image;

    @Override
    public void run(String ...args){
        Optional<BlogUser> existingAdmin = userRepository.findByUsername(username);
        if(existingAdmin.isPresent()) return;

        UserPayloadRequest adminCreationRequest = new UserPayloadRequest(
                username,
                password,
                image,
                RoleEnum.ADMIN
        );
        userService.createUser(adminCreationRequest);
    }
}
