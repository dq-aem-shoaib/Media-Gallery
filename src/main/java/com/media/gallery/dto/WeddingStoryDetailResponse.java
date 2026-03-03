package com.media.gallery.dto;

import java.util.List;

public record WeddingStoryDetailResponse(
        String coupleName,
        String slug,
        List<WeddingStorMediaResponse> images
) {}
