package com.yalantis.manager;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.yalantis.api.ApiSettings;
import com.yalantis.api.deserializer.StringDeserializer;
import com.yalantis.api.services.NetworkService;
import com.yalantis.interfaces.Manager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        //TODO: init services
    }

    /**
     * Api requests
     */

}
