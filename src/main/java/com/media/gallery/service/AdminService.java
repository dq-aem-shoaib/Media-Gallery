package com.media.gallery.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    List<String> saveFiles(List<MultipartFile> files, String username) throws IOException;
}
