package com.media.gallery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/api/v1/admin")
public class AdminController {
    @GetMapping
    public ResponseEntity<String> adminHome() {
        return new ResponseEntity<>("Welcome to admin Home", HttpStatus.OK);
    }
}
