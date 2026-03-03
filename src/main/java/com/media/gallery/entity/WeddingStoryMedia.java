package com.media.gallery.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "wedding_story_media")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class WeddingStoryMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String imageName;

    private int orderIndex;

    private boolean isCover;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_story_id", nullable = false)
    private WeddingStory weddingStory;
}
