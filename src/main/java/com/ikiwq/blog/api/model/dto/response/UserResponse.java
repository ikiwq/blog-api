package com.ikiwq.blog.api.model.dto.response;

import com.ikiwq.blog.api.model.enumeration.RoleEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class UserResponse {
    private long id;
    private String username;
    private RoleEnum role;
    private Instant createdAt;
}
