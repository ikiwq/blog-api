package com.ikiwq.blog.api.config.provider;

import com.ikiwq.blog.api.model.entity.RefreshToken;
import com.ikiwq.blog.api.model.exception.BlogException;
import com.ikiwq.blog.api.model.exception.BlogExceptionEnum;
import com.ikiwq.blog.api.model.security.RefreshAuthenticationToken;
import com.ikiwq.blog.api.repository.RefreshTokenRepository;
import com.ikiwq.blog.api.service.SqlUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {
    private final SqlUserDetailsService sqlUserDetailsService;

    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        String[] splitToken = token.split("\\.");
        if(splitToken.length != 2) {
            throw new BlogException(BlogExceptionEnum.REFRESH_TOKEN_INVALID);
        }

        String identifier = splitToken[0];
        String value = splitToken[1];

        Optional<RefreshToken> optRefreshToken = refreshTokenRepository.findById(identifier);
        if(optRefreshToken.isEmpty()) {
            throw new BlogException(BlogExceptionEnum.REFRESH_TOKEN_INVALID);
        }

        RefreshToken refreshToken = optRefreshToken.get();

        boolean expired = Instant.now().isAfter(refreshToken.getExpiresAt());
        if(!refreshToken.isEnabled() || expired) {
            throw new BlogException(BlogExceptionEnum.REFRESH_TOKEN_INVALID);
        }

        if(!passwordEncoder.matches(value, refreshToken.getValue())) {
            throw new BlogException(BlogExceptionEnum.REFRESH_TOKEN_INVALID);
        }

        String userId = String.valueOf(refreshToken.getUserId());
        UserDetails userDetails = sqlUserDetailsService.loadUserByUsername(userId);

        List<? extends GrantedAuthority> authorities = userDetails.getAuthorities().stream().toList();
        return new RefreshAuthenticationToken(
                value,
                userDetails,
                authorities
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(RefreshAuthenticationToken.class);
    }
}
