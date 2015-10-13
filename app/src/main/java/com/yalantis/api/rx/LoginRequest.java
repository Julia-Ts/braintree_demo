package com.yalantis.api.rx;

import com.yalantis.api.services.TokenLessService;
import com.yalantis.api.task.ApiTask;
import com.yalantis.interfaces.LoginListener;
import com.yalantis.model.example.AuthBody;
import com.yalantis.model.example.AuthDTO;

import retrofit.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Example request for getting Response with RX Java
 * Created by Vadym Pinchuk
 */
public class LoginRequest {

    private TokenLessService mService;
    private String mEmail;
    private String mPassword;
    private LoginListener mLoginListener;

    public LoginRequest(TokenLessService service, String email, String password,
                        LoginListener listener) {
        mService = service;
        mEmail = email;
        mPassword = password;
        mLoginListener = listener;
    }

    public void login() {
        AuthBody body = new AuthBody();
        body.email = mEmail;
        body.password = mPassword;
        mService.loginRx(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Response<AuthDTO>>() {
                    @Override
                    public void call(Response<AuthDTO> response) {
                        if (response.isSuccess()) {
                            mLoginListener.onSuccess(response.body());
                        } else {
                            ApiTask.handleFailure(response);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mLoginListener.onFailure(throwable);
                    }
                });
    }
}