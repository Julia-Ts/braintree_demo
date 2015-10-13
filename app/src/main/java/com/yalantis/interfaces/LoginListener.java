package com.yalantis.interfaces;

import com.yalantis.model.example.AuthDTO;

/**
 * Example listener for login via request handled with RxJava
 *
 * @author Vadym Pinchuk
 */
public interface LoginListener {

    void onSuccess(AuthDTO data);

    void onFailure(Throwable throwable);
}
