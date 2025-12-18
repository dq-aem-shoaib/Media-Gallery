package com.media.gallery.service;

import com.media.gallery.dto.UserInfoDTO;
import com.media.gallery.entity.UserInfo;
import com.media.gallery.model.UserInfoModel;
import com.media.gallery.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserInfoDTO registerUser(UserInfoModel userInfoModel) {
        log.info("Registering new user..");
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoModel, userInfo); // first source, then destination object
        userInfo.setPassword(passwordEncoder.encode(userInfoModel.getPassword()));
        UserInfo savedUserDetails = userInfoRepository.save(userInfo);
        return UserInfoDTO.builder()
                .id(savedUserDetails.getId())
                .name(savedUserDetails.getName())
                .email(savedUserDetails.getEmail())
                .build();
    }
}
