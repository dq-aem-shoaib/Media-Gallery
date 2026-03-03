package com.media.gallery.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaResponseDTO implements Serializable {
    private UUID id;
    private String name;
    private String contentType;
    private String url;
    private Long size;
    private LocalDateTime createdAt;
}
