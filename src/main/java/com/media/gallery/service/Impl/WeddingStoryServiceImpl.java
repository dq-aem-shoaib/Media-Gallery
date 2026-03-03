package com.media.gallery.service.Impl;

import com.media.gallery.dto.*;
import com.media.gallery.entity.WeddingStory;
import com.media.gallery.entity.WeddingStoryMedia;
import com.media.gallery.repository.WeddingStoryMediaRepository;
import com.media.gallery.repository.WeddingStoryRepository;
import com.media.gallery.service.WeddingStoryService;
import com.media.gallery.util.ImageProcessingUtil;
import com.media.gallery.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class WeddingStoryServiceImpl implements WeddingStoryService {

    private final WeddingStoryRepository storyRepo;
    private final WeddingStoryMediaRepository storyMediaRepository;
    private final ImageProcessingUtil imageProcessingUtil;

    // ADMIN
    public void createWeddingStory(CreateWeddingStoryRequest req) {

        WeddingStory story = new WeddingStory();
        story.setCoupleName(req.coupleName());
        story.setSlug(SlugUtil.toSlug(req.coupleName()));
        story.setCreatedAt(LocalDateTime.now());
        story.setActive(req.status());

        AtomicInteger index = new AtomicInteger(0);

        List<WeddingStoryMedia> mediaList =
                IntStream.range(0, req.images().size())
                        .mapToObj(i -> {

                            MultipartFile file = req.images().get(i);
                            WeddingStoryMediaRequest meta = req.weddingStoryImageRequests().get(i);

                            WeddingStoryMedia media = new WeddingStoryMedia();

                            String imagePath = imageProcessingUtil.uploadImage(
                                    file,
                                    SlugUtil.toSlug(req.coupleName()),
                                    meta.isCover()
                            );

                            media.setImageName(imagePath);
                            media.setOrderIndex(meta.orderIndex());
                            media.setCover(meta.isCover());
                            media.setWeddingStory(story);

                            return media;
                        })
                        .toList();
        story.setMedia(mediaList);
        storyRepo.save(story);
    }

    // PUBLIC PREVIEW
    public List<WeddingStoryPreviewResponse> getPreviews() {

        return storyRepo.findAll().stream()
                .filter(WeddingStory::isActive)
                .map(story -> new WeddingStoryPreviewResponse(
                        story.getId(),
                        story.getCoupleName(),
                        story.getSlug(),
                        story.getMedia().stream()
                                .sorted(Comparator.comparingInt(WeddingStoryMedia::getOrderIndex))
                                .map(media -> {
                                    String fileName;
                                    try {
                                        fileName = imageProcessingUtil.getImage(media.getImageName());
                                    } catch (Exception e) {
                                        fileName = "/uploads/default-placeholder.png"; // fallback image
                                    }
                                    return new WeddingStorMediaResponse(media.getId(), fileName, media.getOrderIndex(), media.isCover());
                                })
                                .toList(),
                        story.isActive(),
                        story.getCreatedAt()
                ))
                .toList();
    }

    // FULL STORY
    public WeddingStoryDetailResponse getStoryBySlug(String slug) {

        WeddingStory story = storyRepo
                .findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new RuntimeException("Story not found"));

        List<WeddingStorMediaResponse> images = story.getMedia().stream()
                .sorted(Comparator.comparingInt(WeddingStoryMedia::getOrderIndex))
                .map(media -> {
                    String filename = imageProcessingUtil.getImage(media.getImageName());
                    return new WeddingStorMediaResponse(
                        media.getId(),
                        filename,
                        media.getOrderIndex(),
                            media.isCover()
                    );
                })
                .toList();

        return new WeddingStoryDetailResponse(
                story.getCoupleName(),
                story.getSlug(),
                images
        );
    }

    @Override
    public String updateWeddingStory(
            UUID weddingStoryId,
            CreateWeddingStoryRequest req,
            String loggedInUser
    ) {

        WeddingStory story = storyRepo.findById(weddingStoryId)
                .orElseThrow(() -> new RuntimeException("Wedding story not found"));

        // Update basic fields
        String newSlug = SlugUtil.toSlug(req.coupleName());
        String oldSlug = story.getSlug();

        story.setCoupleName(req.coupleName());
        story.setSlug(newSlug);
        story.setActive(req.status());

        // If images are provided, replace existing media
        if (req.images() != null && !req.images().isEmpty()) {

            // Defensive validation
            if (req.weddingStoryImageRequests() == null ||
                    req.images().size() != req.weddingStoryImageRequests().size()) {
                throw new IllegalArgumentException(
                        "Images and media metadata count must match"
                );
            }

            // Delete existing images from storage
            if (story.getMedia() != null) {
                story.getMedia().forEach(media ->
                        imageProcessingUtil.deleteImage(
                                oldSlug,
                                media.getImageName(),
                                media.isCover()
                        )
                );

                // Remove DB records (orphanRemoval = true)
                story.getMedia().clear();
            }

            List<WeddingStoryMedia> newMedia =
                    IntStream.range(0, req.images().size())
                            .mapToObj(i -> {

                                MultipartFile file = req.images().get(i);
                                WeddingStoryMediaRequest meta = req.weddingStoryImageRequests().get(i);

                                WeddingStoryMedia media = new WeddingStoryMedia();

                                String filePath = imageProcessingUtil.uploadImage(
                                        file,
                                        newSlug,
                                        meta.isCover()
                                );

                                media.setImageName(filePath);
                                media.setOrderIndex(meta.orderIndex());
                                media.setCover(meta.isCover());
                                media.setWeddingStory(story);

                                return media;
                            })
                            .toList();

            story.getMedia().addAll(newMedia);
        }

        storyRepo.save(story);

        return "Wedding story updated successfully";
    }



    @Override
    public void deleteWeddingStory(UUID weddingStoryId, String loggedInUser) {

        WeddingStory story = storyRepo.findById(weddingStoryId)
                .orElseThrow(() -> new RuntimeException("Wedding story not found"));

        // Delete physical images
        story.getMedia().forEach(media ->
                imageProcessingUtil.deleteImage(story.getSlug(), media.getImageName(), media.isCover())
        );

        // Delete story (media removed via cascade)
        storyRepo.delete(story);
    }


    @Override
    public String updateCoverPhoto(String slug, List<MultipartFile> coverPhotos, String loggedInUser) {
        return "";
    }

    @Override
    public void deleteCoverPhoto(String slug, List<MultipartFile> coverPhotos, String loggedInUser) {

    }


}
