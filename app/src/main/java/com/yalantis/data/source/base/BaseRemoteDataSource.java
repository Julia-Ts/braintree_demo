package com.yalantis.data.source.base;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.yalantis.BuildConfig;
import com.yalantis.api.ApiSettings;
import com.yalantis.api.serializer.RepositorySerializer;
import com.yalantis.api.services.GithubService;

import java.io.IOException;

import io.realm.RealmObject;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by irinagalata on 12/1/16.
 */

public abstract class BaseRemoteDataSource implements BaseDataSource {

    protected GithubService mGithubService;
    private Retrofit mRetrofit;

    @Override
    public void init(Context context) {
        initRetrofit();
        initServices();
    }

    private void initServices() {
        mGithubService = mRetrofit.create(GithubService.class);
    }

    private void initRetrofit() {
        HttpLoggingInterceptor.Level level = BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE;
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
//                        .header(ApiSettings.HEADER_AUTH_TOKEN, getAuthToken())
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        }).addInterceptor(new HttpLoggingInterceptor().setLevel(level)).build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiSettings.SERVER)
                .addConverterFactory(createGsonConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(client)
                .build();
    }

    private GsonConverterFactory createGsonConverter() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        builder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getDeclaringClass().equals(RealmObject.class);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        try {
            builder.registerTypeAdapter(Class.forName("io.realm.RepositoryRealmProxy"), new RepositorySerializer());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        return GsonConverterFactory.create(builder.create());
    }

    @Override
    public void clear() {

    }

}
