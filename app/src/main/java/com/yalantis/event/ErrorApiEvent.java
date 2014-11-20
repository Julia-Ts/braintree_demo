package com.yalantis.event;

import android.text.TextUtils;

import com.yalantis.model.ApiError;


/**
 * @author edbaev 22.02.2013
 */
public class ErrorApiEvent extends ApiEvent<ApiError> {

    public String message;
    public boolean displayToast;

    public ErrorApiEvent(ApiError data) {
        super(data);
        this.message = data.message;
        this.displayToast = data.displayToast;
    }

    public ErrorApiEvent(String message, boolean displayToast) {
        super(null);
        this.message = message;
        this.displayToast = !TextUtils.isEmpty(message) && displayToast;
    }
}
