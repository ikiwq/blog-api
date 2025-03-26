package com.ikiwq.blog.api.model.dto.request;

import com.ikiwq.blog.api.model.enumeration.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPayloadRequest {
    private String username;
    private String password;

    private String image;

    private RoleEnum role;
}
