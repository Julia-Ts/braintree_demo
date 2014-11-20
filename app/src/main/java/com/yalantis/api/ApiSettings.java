package com.yalantis.api;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public final class ApiSettings {

    public static final String HOSTNAME = "<your host name.com>";

    public static final String SCHEME = "http://";

    public static final String API_PREFIX = "/api";

    public static final String SERVER = SCHEME + HOSTNAME + API_PREFIX;

    public static final class USER {

        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }
}
