package com.media.gallery.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateWeddingStoryRequest(
        String coupleName,
        List<MultipartFile> images,
        boolean status,
        List<WeddingStoryMediaRequest> weddingStoryImageRequests
) {}
