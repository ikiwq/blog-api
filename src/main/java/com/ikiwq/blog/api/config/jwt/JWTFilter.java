package com.ikiwq.blog.api.config.jwt;

import com.ikiwq.blog.api.model.exception.AuthExceptionEnum;
import com.ikiwq.blog.api.model.exception.BlogException;
import com.ikiwq.blog.api.model.security.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer";

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String[] splitAuthHeader = authHeader.split(" ");
        if(splitAuthHeader.length != 2) {
            throw new BlogException(AuthExceptionEnum.AUTH_TOKEN_INVALID);
        }

        String prefix = splitAuthHeader[0];
        String token = splitAuthHeader[1];

        // Note: When dealing with multiple auth methods, this should be instead replaced
        // with a filterChain.doFilter
        if(!Objects.equals(prefix, BEARER_PREFIX)) {
            throw new BlogException(AuthExceptionEnum.AUTH_METHOD_NOT_SUPPORTED);
        }

        Authentication authentication = authenticationManager.authenticate(
                new JwtAuthenticationToken(token)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
