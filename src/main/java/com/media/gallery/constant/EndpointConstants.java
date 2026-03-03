package com.media.gallery.constant;

public class EndpointConstants {
    // Home controller endpoints
    public static final String HOME_CONTROLLER_API = "/";

    public static final String WEB_API_V1_PREFIX = "/web/api/v1";

    // Authentication Related endpoints
    public static final String LOGIN_API = WEB_API_V1_PREFIX + "/login";
    public static final String REFRESH_TOKEN_API = WEB_API_V1_PREFIX + "/refreshtoken";

    // User related endpoints
    public static final String USER_API_PREFIX = WEB_API_V1_PREFIX + "/user";
    public static final String USER_REGISTRATION = USER_API_PREFIX + "/register";

    // Admin related endpoints
    public static final String ADMIN = WEB_API_V1_PREFIX + "/admin";
    public static final String WEDDING = ADMIN + "/wedding";
    public static final String WEDDING_STORY_REGISTER = WEDDING + "/register";
    public static final String WEDDING_STORY_UPDATE = WEDDING + "/update/{id}";
    public static final String WEDDING_STORY_DELETE = WEDDING + "/delete/{id}";


    // Public related endpoints
    public static final String PUBLIC_WEDDING_STORIES = "/web/api/v1/public/wedding-stories";
    public static final String PUBLIC_PREVIEWS = PUBLIC_WEDDING_STORIES + "/previews";
    public static final String PUBLIC_PREVIEWS_SLUG = PUBLIC_WEDDING_STORIES + "/{slug}";

}
