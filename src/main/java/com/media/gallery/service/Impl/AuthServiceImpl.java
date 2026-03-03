package com.media.gallery.service.Impl;

import com.media.gallery.dto.LoginRequestDTO;
import com.media.gallery.dto.LoginResponseDTO;
import com.media.gallery.dto.RefreshTokenRequestDTO;
import com.media.gallery.dto.RefreshTokenResponseDTO;
import com.media.gallery.entity.RefreshTokenEntity;
import com.media.gallery.entity.UserInfo;
import com.media.gallery.repository.RefreshTokenRepository;
import com.media.gallery.repository.UserInfoRepository;
import com.media.gallery.service.AuthService;
import com.media.gallery.service.JwtTokenGeneratorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    JwtTokenGeneratorService jwtTokenGeneratorService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

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

        RefreshTokenEntity refreshToken = createRefreshToken(userInfo);
        LocalDateTime expiryDate = refreshToken.getExpiryDate();
        userInfo.setFirstLogin(false);

        return LoginResponseDTO.builder()
                .accessToken(token)
                .roleName(roleName)
                .refreshExpiresAt(expiryDate)
                .tokenType("Bearer")
                .refreshToken(refreshToken.getToken())
                .firstLogin(firstLogin)
                .build();
    }

    @Override
    @Transactional
    public RefreshTokenResponseDTO processRefreshTokenRequest(RefreshTokenRequestDTO requestDTO) {

        RefreshTokenEntity existingTokenEntity =
                refreshTokenRepository.findByTokenAndIsActiveTrue(requestDTO.getRefreshToken())
                        .orElseThrow(() ->
                                new BadCredentialsException("Invalid refresh token"));

        if (existingTokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            existingTokenEntity.setIsActive(false);
            throw new BadCredentialsException("Refresh token expired");
        }

        UserInfo userInfo = existingTokenEntity.getUser();

        // 🔐 Rotate refresh token (single-use)
        existingTokenEntity.setIsActive(false);

        // Generate new access token
        String accessToken = jwtTokenGeneratorService.generateToken(userInfo.getEmail(), List.of(userInfo.getRole()));

        // Generate new refresh token
        RefreshTokenEntity newRefreshToken = createRefreshToken(userInfo);

        return RefreshTokenResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .refreshExpiresAt(
                        newRefreshToken.getExpiryDate()
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                )
                .build();
    }

    @Transactional
    private RefreshTokenEntity createRefreshToken(UserInfo user) {

        // Optional: logout from all other sessions
        refreshTokenRepository.deactivateAllTokensForUser(user);

        RefreshTokenEntity token = RefreshTokenEntity.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .isActive(true)
                .build();

        return refreshTokenRepository.save(token);
    }

}
