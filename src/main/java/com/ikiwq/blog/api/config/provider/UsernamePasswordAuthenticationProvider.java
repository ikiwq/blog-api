package com.ikiwq.blog.api.config.provider;

import com.ikiwq.blog.api.model.exception.AuthExceptionEnum;
import com.ikiwq.blog.api.model.exception.BlogException;
import com.ikiwq.blog.api.service.SqlUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private final SqlUserDetailsService sqlUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getCredentials());

        UserDetails userDetails;
        try {
            userDetails = sqlUserDetailsService.loadUserByUsername(username);
        } catch (Exception e){
            throw new BlogException(AuthExceptionEnum.USERNAME_PASSWORD_INVALID);
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BlogException(AuthExceptionEnum.USERNAME_PASSWORD_INVALID);
        }


        List<? extends GrantedAuthority> authorities = userDetails.getAuthorities().stream().toList();
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                authorities
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
