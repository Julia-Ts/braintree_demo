package com.yalantis.model;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public enum ApiError {

    NO_INTERNET(0, "Network connection problem, try again.", true),
    INVALID_EMAIL(6, "Invalid email address.", true),
    INVALID_PASSWORD(1, "Invalid password.", true);

    public final int code;
    public final String message;
    public final boolean displayToast;

    ApiError(int code, String message, boolean displayToast) {
        this.code = code;
        this.message = message;
        this.displayToast = displayToast;
    }

    public static ApiError fromCode(int code) {
        for (ApiError type : ApiError.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

}