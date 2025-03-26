package com.ikiwq.blog.api.service;

import com.ikiwq.blog.api.model.entity.BlogUser;
import com.ikiwq.blog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SqlUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<BlogUser> userOpt;
        try {
            long userId = Long.parseLong(username);
            userOpt = userRepository.findById(userId);
        } catch(NumberFormatException e) {
            userOpt = userRepository.findByUsername(username);
        }

        if(userOpt.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        BlogUser blogUser = userOpt.get();

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + blogUser.getRole().name());
        List<GrantedAuthority> authorities = List.of(authority);

        return User.builder()
                .username(blogUser.getId().toString())
                .password(blogUser.getPassword())
                .authorities(authorities)
                .build();
    }
}
