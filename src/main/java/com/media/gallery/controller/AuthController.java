package com.media.gallery.controller;

import com.media.gallery.dto.LoginRequestDTO;
import com.media.gallery.dto.LoginResponseDTO;
import com.media.gallery.dto.RefreshTokenRequestDTO;
import com.media.gallery.dto.RefreshTokenResponseDTO;
import com.media.gallery.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.media.gallery.constant.EndpointConstants.LOGIN_API;
import static com.media.gallery.constant.EndpointConstants.REFRESH_TOKEN_API;

@RestController
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(LOGIN_API)
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequest) {
        log.info("Username and password,{},{}", loginRequest.getUsername(), loginRequest.getPassword());
        return new ResponseEntity<>(authService.authenticateUser(loginRequest), HttpStatus.OK);
    }

    @PostMapping(REFRESH_TOKEN_API)
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO requestDTO) {
        log.info("Processing refresh token..");
        RefreshTokenResponseDTO refreshTokenResponse = authService.processRefreshTokenRequest(requestDTO);
        return ResponseEntity.ok(refreshTokenResponse);
    }

}
