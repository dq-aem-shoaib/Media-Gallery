package com.media.gallery.dto;

import java.util.UUID;

public record WeddingStorMediaResponse(
        UUID id,
        String image,
        int orderIndex,
        boolean isCover
) {}
