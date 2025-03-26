package com.ikiwq.blog.api.model.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class RefreshAuthenticationToken extends AbstractAuthenticationToken {
    private final UserDetails principal;
    private final String token;

    public RefreshAuthenticationToken(String token){
        super(null);
        this.token = token;
        this.principal = null;
        setAuthenticated(false);
    }

    public RefreshAuthenticationToken(
            String token,
            UserDetails principal,
            List<? extends GrantedAuthority> grantedAuthorities
    ) {
        super(grantedAuthorities);
        this.token = token;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials(){
        return this.token;
    }

    @Override
    public Object getPrincipal(){
        return this.principal;
    }
}
