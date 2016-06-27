package com.yalantis.manager;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.yalantis.api.ApiSettings;
import com.yalantis.api.LogOutWhenSessionExpired;
import com.yalantis.api.body.ErrorBody;
import com.yalantis.api.body.ResultBody;
import com.yalantis.api.deserializer.StringDeserializer;
import com.yalantis.api.services.NetworkService;
import com.yalantis.interfaces.Manager;
import com.yalantis.model.SomeModel;

import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ApiManager implements Manager {

    private NetworkService mNetworkService;

    @Override
    public void init(Context context) {
        initServices(createRetrofit(createOkHttpClient()));
    }

    @Override
    public void clear() {

    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(interceptor);

        return builder.build();
    }

    private Retrofit createRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(ApiSettings.SERVER)
                .addConverterFactory(createGsonConverter())
                .client(client)
                .build();
    }

    private GsonConverterFactory createGsonConverter() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        builder.registerTypeAdapter(String.class, new StringDeserializer());
        return GsonConverterFactory.create(builder.create());
    }

    private void initServices(Retrofit retrofit) {
        mNetworkService = retrofit.create(NetworkService.class);
        //TODO: init services
    }

    public Observable<SomeModel> someApiCall() {
        return mNetworkService.someApiCall()
                .subscribeOn(Schedulers.io())
                .map(new Func1<ResultBody<SomeModel>, SomeModel>() {
                    @Override
                    public SomeModel call(ResultBody<SomeModel> body) {
                        return body.getResult();
                    }
                })
                .retryWhen(new LogOutWhenSessionExpired())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
