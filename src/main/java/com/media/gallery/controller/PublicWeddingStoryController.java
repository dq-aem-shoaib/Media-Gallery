package com.media.gallery.controller;

import com.media.gallery.dto.WeddingStoryDetailResponse;
import com.media.gallery.dto.WeddingStoryPreviewResponse;
import com.media.gallery.service.WeddingStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.media.gallery.constant.EndpointConstants.*;

@RestController
@RequiredArgsConstructor
public class PublicWeddingStoryController {

    private final WeddingStoryService service;

    @GetMapping(PUBLIC_PREVIEWS)
    public ResponseEntity<List<WeddingStoryPreviewResponse>> previews() {
            List<WeddingStoryPreviewResponse> previews = service.getPreviews();
        return ResponseEntity.ok(previews);
    }

    @GetMapping(PUBLIC_PREVIEWS_SLUG)
    public ResponseEntity<WeddingStoryDetailResponse> detail(@PathVariable String slug) {
            WeddingStoryDetailResponse storyBySlug = service.getStoryBySlug(slug);
        return ResponseEntity.ok(storyBySlug);
    }
}
