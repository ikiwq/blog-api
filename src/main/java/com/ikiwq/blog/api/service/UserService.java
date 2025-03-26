package com.ikiwq.blog.api.service;

import com.ikiwq.blog.api.model.dto.request.UserPayloadRequest;
import com.ikiwq.blog.api.model.dto.response.UserResponse;
import com.ikiwq.blog.api.model.entity.BlogUser;
import com.ikiwq.blog.api.model.exception.BlogException;
import com.ikiwq.blog.api.model.exception.BlogExceptionEnum;
import com.ikiwq.blog.api.model.mapper.UserMapper;
import com.ikiwq.blog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponse createUser(UserPayloadRequest creationRequest){
        if(userRepository.findByUsername(creationRequest.getUsername()).isPresent()){
            throw new BlogException(BlogExceptionEnum.USER_USERNAME_ALREADY_TAKEN);
        }

        BlogUser blogUser = userMapper.toEntity(creationRequest);
        return userMapper.toResponse(userRepository.save(blogUser));
    }
}
