package com.media.gallery;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MediaGalleryApplication {

	public static Logger log = LoggerFactory.getLogger(MediaGalleryApplication.class);

	@PostConstruct
	public void init(){
		log.info("Application started....");
	}

	public static void main(String[] args) {
		log.info("Application executed...");
		SpringApplication.run(MediaGalleryApplication.class, args);
	}

}
