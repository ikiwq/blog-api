package com.ikiwq.blog.api.model.dto.request;

import com.ikiwq.blog.api.model.enumeration.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPayloadRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;

    private String image;

    @NotBlank(message = "Role is required")
    private RoleEnum role;
}
