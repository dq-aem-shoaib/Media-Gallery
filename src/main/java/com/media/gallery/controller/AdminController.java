package com.media.gallery.controller;

import com.media.gallery.dto.CustomUserDetails;
import com.media.gallery.dto.MediaResponseDTO;
import com.media.gallery.entity.MediaFileEntity;
import com.media.gallery.entity.UserInfo;
import com.media.gallery.repository.MediaRepository;
import com.media.gallery.repository.UserInfoRepository;
import com.media.gallery.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/web/api/v1/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadMedia(
            @RequestPart("files") List<MultipartFile> files,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        log.info("Saving media files to server..");
        List<String> filenames = adminService.saveFiles(files, userDetails.getUsername());
        return ResponseEntity.ok(filenames);
    }

    @GetMapping("/public/media/{id}")
    public ResponseEntity<Resource> viewMediaPublic(
            @PathVariable UUID id) {

        MediaFileEntity media = mediaRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Media not found"));

        Path filePath = Paths.get(media.getFilePath()).normalize();

        if (!Files.exists(filePath)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "File not found on server");
        }

        Resource resource = new FileSystemResource(filePath);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.getContentType()))
                .header(HttpHeaders.CACHE_CONTROL, "private, max-age=3600")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }

    @GetMapping("/media")
    public ResponseEntity<Page<MediaResponseDTO>> getAllMyMedia(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        UserInfo user = userInfoRepository
                .findByEmail(principal.getUsername())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<MediaResponseDTO> response = mediaRepository
                .findAllByUser(user, pageable)
                .map(media -> MediaResponseDTO.builder()
                        .id(media.getId())
                        .name(media.getOriginalName())
                        .contentType(media.getContentType())
                        .size(media.getSize())
                        .createdAt(media.getCreatedAt())
                        .url("/web/api/v1/admin/media/" + media.getId())
                        .build());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Secue");
    }


}
