package com.ikiwq.blog.api.service;

import com.ikiwq.blog.api.config.jwt.JWTUtils;
import com.ikiwq.blog.api.model.dto.request.LoginRequest;
import com.ikiwq.blog.api.model.dto.request.RefreshRequest;
import com.ikiwq.blog.api.model.dto.response.LoginResponse;
import com.ikiwq.blog.api.model.security.GeneratedToken;
import com.ikiwq.blog.api.model.security.RefreshAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    private final JWTUtils jwtUtils;

    public LoginResponse login(LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        long userId = Long.parseLong(userDetails.getUsername());

        GeneratedToken accessToken = jwtUtils.generateToken(userDetails);
        GeneratedToken refreshToken = refreshTokenService.createRefreshToken(userId);

        return new LoginResponse(
                accessToken.getToken(),
                accessToken.getExpiresAt(),
                refreshToken.getToken(),
                refreshToken.getExpiresAt()
        );
    }

    public LoginResponse refresh(RefreshRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new RefreshAuthenticationToken(request.getRefreshToken())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        refreshTokenService.invalidateToken(request.getRefreshToken());

        long userId = Long.parseLong(userDetails.getUsername());
        GeneratedToken accessToken = jwtUtils.generateToken(userDetails);
        GeneratedToken refreshToken = refreshTokenService.createRefreshToken(userId);

        return new LoginResponse(
                accessToken.getToken(),
                accessToken.getExpiresAt(),
                refreshToken.getToken(),
                refreshToken.getExpiresAt()
        );
    }
}
