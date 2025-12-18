package com.media.gallery.controller;

import com.media.gallery.dto.UserInfoDTO;
import com.media.gallery.model.UserInfoModel;
import com.media.gallery.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.media.gallery.constant.EndpointConstants.USER_REGISTRATION;

@RestController
public class UserController {

    @Autowired
    private UserInfoService userService;

    @PostMapping(USER_REGISTRATION)
    public ResponseEntity<UserInfoDTO> registerUser(@RequestBody UserInfoModel userInfoModel) {
        return new ResponseEntity<>(userService.registerUser(userInfoModel), HttpStatus.CREATED);
    }

}
