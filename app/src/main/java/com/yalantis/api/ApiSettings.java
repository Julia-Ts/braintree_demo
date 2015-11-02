package com.yalantis.api;

/**
 * Class for constants, used for URL completing for REST requests
 */
public final class ApiSettings {

    public static final String SCHEME = "https://";

    public static final String HOSTNAME = "api.github.com/";

    public static final String API_PREFIX = "api/";

    public static final String SERVER = SCHEME + HOSTNAME + API_PREFIX;

    public static final String HEADER_AUTH_TOKEN = "Authorization";

    public static final String PATH_ORGANIZATION = "organization";
    public static final String PARAM_REPOS_TYPE = "type";
    public static final String ORGANIZATION_REPOS = "orgs/" + "{" + PATH_ORGANIZATION + "}" + "/repos/";


}