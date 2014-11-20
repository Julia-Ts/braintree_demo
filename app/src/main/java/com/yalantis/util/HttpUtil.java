package com.yalantis.util;

import java.util.regex.Pattern;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public class HttpUtil {

    private static final String PATTERN_EMAIL = "^([\\w]+)(([-\\.\\+][\\w]+)?)*@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    public static boolean isEmail(final String email) {
        return email != null && Pattern.matches(PATTERN_EMAIL, email);
    }

}
