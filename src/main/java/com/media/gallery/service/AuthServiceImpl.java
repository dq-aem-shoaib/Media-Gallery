package com.media.gallery.service;

import com.media.gallery.dto.LoginRequestDTO;
import com.media.gallery.dto.LoginResponseDTO;
import com.media.gallery.entity.UserInfo;
import com.media.gallery.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    JwtTokenGeneratorService jwtTokenGeneratorService;

    @Override
    @Transactional
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequestDTO) {
        String inputKey = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(inputKey, password));
        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Invalid username/email or password.");
        }

        UserInfo userInfo = userInfoRepository.findByEmail(inputKey)
                .orElseGet(() -> userInfoRepository.findByName(inputKey)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "User not found by given ID, Mobile or Email ID")));

        String roleName = userInfo.getRole();
        boolean firstLogin = userInfo.isFirstLogin();

        String token = jwtTokenGeneratorService.generateToken(
                userInfo.getEmail(),
                List.of(roleName)
        );

//        LocalDateTime expiryDate = refreshToken.getExpiryDate();
        userInfo.setFirstLogin(false);

        return LoginResponseDTO.builder()
                .accessToken(token)
                .roleName(roleName)
                .refreshExpiresAt(LocalDateTime.now())
                .tokenType("Bearer")
                .refreshToken("refreshToken.getToken()")
                .firstLogin(firstLogin)
                .build();
    }
}
