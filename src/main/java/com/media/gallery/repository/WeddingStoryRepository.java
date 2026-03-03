package com.media.gallery.repository;

import com.media.gallery.entity.WeddingStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WeddingStoryRepository extends JpaRepository<WeddingStory, UUID> {

    Optional<WeddingStory> findBySlugAndActiveTrue(String slug);
}
