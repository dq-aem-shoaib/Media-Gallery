package com.media.gallery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO implements Serializable {

    private String accessToken;
    private String refreshToken;
    private LocalDateTime refreshExpiresAt;
    private String tokenType = "Bearer";

    private String roleName;
    private List<String> permissions;

    private boolean firstLogin;
}
