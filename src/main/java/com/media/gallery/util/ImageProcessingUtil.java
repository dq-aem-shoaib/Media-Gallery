package com.media.gallery.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Component
public class ImageProcessingUtil {

    @Value("${storage.local.upload-dir}")
    private String uploadLocation;

    public String getImage(String fileName){
        try{
            Path filePath = Paths.get(fileName).normalize();

            if (!Files.exists(filePath)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "File not found on server");
            }

            Resource resource = new FileSystemResource(filePath);

            return resource.getFilename();}
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String uploadImage(MultipartFile file, String slug, boolean isCover) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {
            Path baseDir = Paths.get(uploadLocation)
                    .toAbsolutePath()
                    .resolve("users")
                    .resolve(slug);

            // If cover image → store in /cover subfolder
            if (isCover) {
                baseDir = baseDir.resolve("cover");
            }

            Files.createDirectories(baseDir);

            String storedName =
                    UUID.randomUUID() + "_" +
                            StringUtils.cleanPath(
                                    Objects.requireNonNull(file.getOriginalFilename())
                            );

            Path destination = baseDir.resolve(storedName);

            if (!Files.exists(destination)) {
                Files.copy(file.getInputStream(), destination);
            }

            return destination.toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }


    public void deleteImage(String slug, String imagePath, boolean isCover) {

        try {
            Path baseDir = Paths.get(uploadLocation)
                    .toAbsolutePath()
                    .resolve("users")
                    .resolve(slug);

            if (isCover) {
                baseDir = baseDir.resolve("cover");
            }

            Path filePath = baseDir.resolve(Paths.get(imagePath).getFileName());
            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }



}
