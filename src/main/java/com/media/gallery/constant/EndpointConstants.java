package com.media.gallery.constant;

public class EndpointConstants {
    // Home controller endpoints
    public static final String HOME_CONTROLLER_API = "/";

    public static final String WEB_API_V1_PREFIX = "/web/api/v1";

    // Authentication Related endpoints
    public static final String LOGIN_API = WEB_API_V1_PREFIX + "/login";

    // User related endpoints
    public static final String USER_API_PREFIX = WEB_API_V1_PREFIX + "/user";
    public static final String USER_REGISTRATION = USER_API_PREFIX + "/register";
}
