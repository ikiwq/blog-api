package com.ikiwq.blog.api.config;

import com.ikiwq.blog.api.config.provider.JwtAuthenticationProvider;
import com.ikiwq.blog.api.config.provider.RefreshTokenAuthenticationProvider;
import com.ikiwq.blog.api.config.provider.UsernamePasswordAuthenticationProvider;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public AuthenticationManager authenticationManager(
            UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider,
            JwtAuthenticationProvider jwtAuthenticationProvider,
            RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider
    ) {
        return new ProviderManager(
                usernamePasswordAuthenticationProvider,
                jwtAuthenticationProvider,
                refreshTokenAuthenticationProvider
        );
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecretKey signingKey() {
        return Jwts.SIG.HS256.key().build();
    }
}
