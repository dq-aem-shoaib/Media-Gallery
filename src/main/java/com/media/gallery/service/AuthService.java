package com.media.gallery.service;

import com.media.gallery.dto.LoginRequestDTO;
import com.media.gallery.dto.LoginResponseDTO;

public interface AuthService {
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequestDTO);
}
