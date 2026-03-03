package com.media.gallery.controller;

import com.media.gallery.dto.CreateWeddingStoryRequest;
import com.media.gallery.service.WeddingStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.media.gallery.constant.EndpointConstants.*;

@RestController
@RequiredArgsConstructor
public class AdminWeddingStoryController {

    private final WeddingStoryService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(WEDDING_STORY_REGISTER)
    public ResponseEntity<String> create(
            @ModelAttribute CreateWeddingStoryRequest request) {

        service.createWeddingStory(request);
        return ResponseEntity.ok("Successfully created!!!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(WEDDING_STORY_UPDATE)
    public ResponseEntity<?> update( @AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable UUID id,
                                     @ModelAttribute CreateWeddingStoryRequest storyRequest){
        String msg = service.updateWeddingStory(id, storyRequest, userDetails.getUsername());
        return ResponseEntity.ok(msg);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(WEDDING_STORY_DELETE)
    public ResponseEntity<String> delete(@PathVariable UUID id,
                                         @AuthenticationPrincipal UserDetails userDetails){
        service.deleteWeddingStory(id, userDetails.getUsername());
        return ResponseEntity.ok("Record Deleted Successfully");
    }
}
