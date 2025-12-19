package com.media.gallery.repository;

import com.media.gallery.entity.RefreshTokenEntity;
import com.media.gallery.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByTokenAndIsActiveTrue(String token);

    @Modifying
    @Query("""
        UPDATE RefreshTokenEntity rt
        SET rt.isActive = false
        WHERE rt.user = :user
    """)
    void deactivateAllTokensForUser(@Param("user") UserInfo user);
}
