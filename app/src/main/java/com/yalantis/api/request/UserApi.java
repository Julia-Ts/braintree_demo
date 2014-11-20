package com.yalantis.api.request;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

import com.yalantis.api.ApiSettings;
import com.yalantis.model.dto.GetUserDTO;

/**
 * Created by Ed
 */
public interface UserApi {

    @POST("/")
    @FormUrlEncoded
    public void getUser(@Field(ApiSettings.USER.PASSWORD) String password,
            Callback<GetUserDTO> callback);

}
