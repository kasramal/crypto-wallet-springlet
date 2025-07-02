package com.demohouse.authentication.security;

import ir.kavoshgaran.authentication.configuration.properties.AuthenticationProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service("customUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthenticationProperties authenticationProperties;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(AuthenticationProperties authenticationProperties, PasswordEncoder passwordEncoder) {
        this.authenticationProperties = authenticationProperties;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<AuthenticationProperties.User> users = authenticationProperties.getUsers();
        for (AuthenticationProperties.User user : users) {
            if (user.getUsername().equals(s))
                return new User(
                        user.getUsername(),
                        this.passwordEncoder.encode(user.getPassword()),
                        Collections.singleton(new SimpleGrantedAuthority("ADMIN"))
                );
        }
        throw new UsernameNotFoundException("Username not found");
    }
}
