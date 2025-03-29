package com.ikiwq.blog.api.service;

import com.ikiwq.blog.api.model.entity.RefreshToken;
import com.ikiwq.blog.api.model.exception.AuthExceptionEnum;
import com.ikiwq.blog.api.model.exception.BlogException;
import com.ikiwq.blog.api.model.security.GeneratedToken;
import com.ikiwq.blog.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${auth.jwt.refreshTokenExpirationTime}")
    private long refreshTokenExpirationTimeSeconds;

    public GeneratedToken createRefreshToken(long userId) {
        String tokenId = UUID.randomUUID().toString();
        String value = UUID.randomUUID().toString();
        String encryptedValue = passwordEncoder.encode(value);

        Instant expiresAt = Instant.now().plus(refreshTokenExpirationTimeSeconds, ChronoUnit.SECONDS);
        RefreshToken refreshTokenToSave = new RefreshToken(
                tokenId,
                encryptedValue,
                userId,
                expiresAt
        );

        refreshTokenRepository.save(refreshTokenToSave);

        String tokenValue = String.format("%s.%s", tokenId, value);
        return new GeneratedToken(
                tokenValue,
                expiresAt
        );
    }

    public void invalidateToken(String token) {
        String[] splitToken = token.split("\\.");
        if(splitToken.length != 2){
            throw new BlogException(AuthExceptionEnum.REFRESH_TOKEN_INVALID);
        }

        RefreshToken refreshToken = refreshTokenRepository.findById(splitToken[0]).orElseThrow(
                () -> new BlogException(AuthExceptionEnum.REFRESH_TOKEN_INVALID)
        );
        refreshToken.setEnabled(false);

        refreshTokenRepository.save(refreshToken);
    }
}
