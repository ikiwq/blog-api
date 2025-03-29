package com.ikiwq.blog.api.config;

import com.ikiwq.blog.api.model.security.GeneratedToken;
import com.ikiwq.blog.api.model.security.JwtAuthenticationToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.ikiwq.blog.api.util.AuthenticationUtils.extractAuthorities;
import static com.ikiwq.blog.api.util.AuthenticationUtils.extractClaims;

@Component
@RequiredArgsConstructor
public class JWTUtils {
    @Value("${auth.jwt.accessTokenExpirationTime}")
    private long jwtExpirationTimeSeconds;

    private final SecretKey signingKey;

    public Authentication parseToken(String jwt) {
        JwtParser jwtParser = Jwts.parser().verifyWith(signingKey).build();

        Claims claims = jwtParser.parseSignedClaims(jwt).getPayload();
        String subject = claims.getSubject();

        List<GrantedAuthority> grantedAuthorities = extractAuthorities(claims);

        UserDetails userDetails = User.builder()
                .username(subject)
                .authorities(grantedAuthorities)
                .password(jwt)
                .build();

        return new JwtAuthenticationToken(jwt, userDetails, grantedAuthorities);
    }

    public GeneratedToken generateToken(UserDetails userDetails) {
        Date issuedAt = Date.from(Instant.now());

        Instant expiresAt = Instant.now().plus(jwtExpirationTimeSeconds, ChronoUnit.SECONDS);
        Date expiration = Date.from(expiresAt);

        Map<String, Set<String>> claims = extractClaims(userDetails);

        String token = Jwts
                .builder()
                .signWith(signingKey)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .subject(userDetails.getUsername())
                .claims(claims)
                .compact();

        return new GeneratedToken(token, expiresAt);
    }
}
