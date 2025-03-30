package com.ikiwq.blog.api.util;

import com.ikiwq.blog.api.model.exception.BlogException;
import com.ikiwq.blog.api.model.exception.BlogExceptionEnum;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class AuthenticationUtils {
    public static List<GrantedAuthority> extractAuthorities(Claims claims) {
        List<?> authorities = (List<?>) claims.get("roles");

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(Object authority : authorities){
            grantedAuthorities.add(new SimpleGrantedAuthority((String) authority));
        }

        return grantedAuthorities;
    }

    public static Map<String, Set<String>> extractClaims(UserDetails userDetails) {
        Map<String, Set<String>> claims = new HashMap<>();
        claims.putIfAbsent("roles", new HashSet<>());

        for(GrantedAuthority grantedAuthority : userDetails.getAuthorities()){
            String authority = grantedAuthority.getAuthority();
            claims.get("roles").add(authority);
        }

        return claims;
    }

    public static long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            return (long) authentication.getPrincipal();
        }
        throw new BlogException(BlogExceptionEnum.USER_NOT_AUTHENTICATED);
    }
}
