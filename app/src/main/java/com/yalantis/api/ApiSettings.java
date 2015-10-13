package com.yalantis.api;

/**
 * Class for constants, used for URL completing for REST requests
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public final class ApiSettings {

    /**
     * Base Url MUST ends with slash "/"
     * URL should NOT start with slash "/"
     */

    public static final String HOSTNAME = "<your host name.com>/";

    public static final String SCHEME = "http://";

    public static final String API_PREFIX = "api/";

    public static final String SERVER = SCHEME + HOSTNAME + API_PREFIX;

    public static final String HEADER_AUTH_TOKEN = "Authorization";

    public static final String AUTH_TOKEN_PREFIX = "Token ";

    public static final String LOGIN = "auth/login/";
}