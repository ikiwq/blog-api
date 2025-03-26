package com.ikiwq.blog.api.util;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
}
