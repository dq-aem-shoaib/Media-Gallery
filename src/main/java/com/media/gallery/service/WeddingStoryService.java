package com.media.gallery.service;

import com.media.gallery.dto.CreateWeddingStoryRequest;
import com.media.gallery.dto.WeddingStoryDetailResponse;
import com.media.gallery.dto.WeddingStoryPreviewResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface WeddingStoryService {
    public void createWeddingStory(CreateWeddingStoryRequest req);
    public List<WeddingStoryPreviewResponse> getPreviews();
    public WeddingStoryDetailResponse getStoryBySlug(String slug);
    public String updateWeddingStory(UUID weddingStoryId, CreateWeddingStoryRequest req, String loggedInUser);
    public void deleteWeddingStory(UUID weddingStoryId, String loggedInUser);
    public String updateCoverPhoto(String slug, List<MultipartFile> coverPhotos,String loggedInUser);
    public void deleteCoverPhoto(String slug, List<MultipartFile> coverPhotos,String loggedInUser);
}
