package com.ikiwq.blog.api.config;

import com.ikiwq.blog.api.config.jwt.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class FilterChainConfig {
    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/v1/articles/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/files/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/files/*").hasRole("ADMIN")
                        .requestMatchers("/api/v1/users/*").hasRole("ADMIN")
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
