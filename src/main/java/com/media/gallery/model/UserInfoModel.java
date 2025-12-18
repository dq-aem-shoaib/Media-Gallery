package com.media.gallery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoModel implements Serializable {
    private String name;
    private String email;
    private String role;
    private String password;
}
