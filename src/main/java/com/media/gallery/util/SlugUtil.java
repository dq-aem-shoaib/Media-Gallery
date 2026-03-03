package com.media.gallery.util;

public class SlugUtil {

    public static String toSlug(String text) {
        return text.toLowerCase()
                .replace("&", "and")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
