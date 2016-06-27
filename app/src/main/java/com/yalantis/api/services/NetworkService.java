package com.yalantis.api.services;

import com.yalantis.api.ApiSettings;
import com.yalantis.api.body.ResultBody;
import com.yalantis.model.SomeModel;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by voltazor on 27/06/16.
 */
public interface NetworkService {

    @GET(ApiSettings.SOME_API_CALL)
    Observable<ResultBody<SomeModel>> someApiCall();

}
