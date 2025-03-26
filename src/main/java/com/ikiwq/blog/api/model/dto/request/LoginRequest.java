package com.ikiwq.blog.api.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
   private String username;
   private String password;
}
