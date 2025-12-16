package com.media.gallery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.media.gallery.constant.EndpointConstants.HOME_CONTROLLER_API;

@RestController
@RequestMapping(HOME_CONTROLLER_API)
public class HomeController {

    @GetMapping
    public ResponseEntity<String> galleryHome() {
        return new ResponseEntity<>("Welcome to Media Gallery Application", HttpStatus.OK);
    }

}
