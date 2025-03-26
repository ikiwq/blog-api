package com.ikiwq.blog.api.model.mapper;

import com.ikiwq.blog.api.model.dto.request.UserPayloadRequest;
import com.ikiwq.blog.api.model.dto.response.UserResponse;
import com.ikiwq.blog.api.model.entity.BlogUser;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;

@Mapper(
        componentModel = "spring",
        imports={Instant.class}
)
public abstract class UserMapper {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Named("hashPassword")
    protected String hashPassword(String password){
        return bCryptPasswordEncoder.encode(password);
    }

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "password", qualifiedByName = "hashPassword"),
            @Mapping(target = "articles", ignore = true),
            @Mapping(target = "createdAt", expression = "java(Instant.now())")
    })
    public abstract BlogUser toEntity(UserPayloadRequest creationRequest);

    public abstract UserResponse toResponse(BlogUser blogUser);
}
