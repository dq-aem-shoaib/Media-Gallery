package com.media.gallery.service.Impl;

import com.media.gallery.entity.MediaFileEntity;
import com.media.gallery.entity.UserInfo;
import com.media.gallery.repository.MediaRepository;
import com.media.gallery.repository.UserInfoRepository;
import com.media.gallery.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Value("${storage.local.upload-dir}")
    private String uploadLocation;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    @Transactional
    public List<String> saveFiles(List<MultipartFile> files, String username) throws IOException {

        List<MediaFileEntity> mediaFiles = new ArrayList<>();

        UserInfo user = userInfoRepository.findByEmail(username).get();

        Path userDir = Paths.get(uploadLocation).toAbsolutePath().resolve("users")
                .resolve(String.valueOf(user.getId()));
        Files.createDirectories(userDir);

        for (MultipartFile file : files) {

            if (file.isEmpty()) continue;

            String storedName = UUID.randomUUID() + "_" +
                    StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            Path destination = userDir.resolve(storedName);
            Files.copy(file.getInputStream(), destination);

            MediaFileEntity media = new MediaFileEntity();
            media.setOriginalName(file.getOriginalFilename());
            media.setStoredName(storedName);
            media.setContentType(file.getContentType());
            media.setSize(file.getSize());
            media.setFilePath(destination.toString());
            media.setUser(user);

            mediaFiles.add(media);
        }
        return mediaRepository.saveAll(mediaFiles).stream().map(file -> file.getId().toString()).toList();
    }

}
