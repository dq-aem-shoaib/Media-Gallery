package com.media.gallery;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

//import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MediaGallaeyApplicationTests {

	public static Logger log = LoggerFactory.getLogger(MediaGallaeyApplicationTests.class);

	@Test
	void contextLoads() {
		log.info("Test cases executing...");
		assertEquals(true,true);
		log.info("Test completed....");
	}

}
