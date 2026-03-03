package com.media.gallery.repository;

import com.media.gallery.entity.MediaFileEntity;
import com.media.gallery.entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<MediaFileEntity, UUID> {
    Optional<MediaFileEntity> findByIdAndUser(UUID id, UserInfo user);
    Page<MediaFileEntity> findAllByUser(UserInfo user, Pageable pageable);}

