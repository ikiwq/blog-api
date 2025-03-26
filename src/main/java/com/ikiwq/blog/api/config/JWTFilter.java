package com.ikiwq.blog.api.config;

import com.ikiwq.blog.api.model.security.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${auth.jwt.accessTokenCookie}")
    private final static String accessTokenCookieName = "blog.auth";
    @Value("${auth.jwt.refreshTokenCookie}")
    private final static String refreshTokenCookieName = "blog.refresh";

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        Cookie accessTokenCookie = null;

        if(cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        for(Cookie cookie : cookies){
            if(Objects.equals(cookie.getName(), accessTokenCookieName)){
                accessTokenCookie = cookie;
            }
        }

        if(accessTokenCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = authenticationManager.authenticate(
                new JwtAuthenticationToken(accessTokenCookie.getValue())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
