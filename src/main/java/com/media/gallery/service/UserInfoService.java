package com.media.gallery.service;

import com.media.gallery.dto.UserInfoDTO;
import com.media.gallery.model.UserInfoModel;

public interface UserInfoService {
    public UserInfoDTO registerUser(UserInfoModel userInfoModel);
}
