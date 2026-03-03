package com.media.gallery.repository;

import com.media.gallery.entity.WeddingStoryMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WeddingStoryMediaRepository extends JpaRepository<WeddingStoryMedia, UUID> {


}
