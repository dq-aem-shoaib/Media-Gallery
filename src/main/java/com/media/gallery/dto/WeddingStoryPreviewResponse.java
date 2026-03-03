package com.media.gallery.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record WeddingStoryPreviewResponse(
        UUID id,
        String coupleName,
        String slug,
        List<WeddingStorMediaResponse> images,
        boolean active,
        LocalDateTime createdAt
) {}
