package com.media.gallery.service;

import com.media.gallery.entity.UserInfo;
import com.media.gallery.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByName(username)
                .or(() -> userInfoRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + username));
        if (!userInfo.isAccountActive()) {
            throw new DisabledException("User account is disabled");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userInfo.getRole());
        return new org.springframework.security.core.userdetails.User(
                userInfo.getEmail(), userInfo.getPassword(), Collections.singleton(authority));
    }
}
