package com.media.gallery.entity;

import jakarta.persistence.*;

import java.util.List;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wedding_story")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class WeddingStory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String coupleName;

    @Column(nullable = false, unique = true)
    private String slug;

    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(
            mappedBy = "weddingStory",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("orderIndex ASC")
    private List<WeddingStoryMedia> media;
}
