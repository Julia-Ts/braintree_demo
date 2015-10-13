package com.yalantis.interfaces;

import com.yalantis.model.example.AuthDTO;

import retrofit.Response;

/**
 * Example listener for login via async request
 * Created by Vadym Pinchuk
 */
public interface CallbackListener {

    void onSuccess(Response<AuthDTO> response);

    void onFailure(Throwable error);

}