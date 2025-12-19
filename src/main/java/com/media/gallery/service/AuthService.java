package com.media.gallery.service;

import com.media.gallery.dto.LoginRequestDTO;
import com.media.gallery.dto.LoginResponseDTO;
import com.media.gallery.dto.RefreshTokenRequestDTO;
import com.media.gallery.dto.RefreshTokenResponseDTO;

public interface AuthService {
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequestDTO);

    RefreshTokenResponseDTO processRefreshTokenRequest(RefreshTokenRequestDTO requestDTO);
}
