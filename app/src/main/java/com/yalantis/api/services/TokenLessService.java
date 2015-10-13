package com.yalantis.api.services;

import com.yalantis.api.ApiSettings;
import com.yalantis.model.example.AuthBody;
import com.yalantis.model.example.AuthDTO;

import retrofit.Call;
import retrofit.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Service class with requests to server
 * Examples display three cases of use request
 * clocking, not blocking and RxJava
 * Created by Vadym Pinchuk.
 */
public interface TokenLessService {

    // Can be blocking and not blocking
    @POST(ApiSettings.LOGIN)
    Call<AuthDTO> login(@Body AuthBody authBody);

    // RxJava Observable instead Call
    @POST(ApiSettings.LOGIN)
    Observable<Response<AuthDTO>> loginRx(@Body AuthBody authBody);

}