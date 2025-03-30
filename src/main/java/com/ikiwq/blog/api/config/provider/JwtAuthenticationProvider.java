package com.ikiwq.blog.api.config.provider;

import com.ikiwq.blog.api.config.jwt.JWTUtils;
import com.ikiwq.blog.api.model.exception.AuthExceptionEnum;
import com.ikiwq.blog.api.model.exception.BlogException;
import com.ikiwq.blog.api.model.security.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JWTUtils jwtUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();

        try {
            return jwtUtils.parseToken(token);
        } catch (Exception e) {
            throw new BlogException(AuthExceptionEnum.AUTH_TOKEN_INVALID);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
