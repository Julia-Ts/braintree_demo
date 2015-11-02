package com.yalantis.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.yalantis.api.ApiSettings;
import com.yalantis.api.serializer.RepositorySerializer;
import com.yalantis.api.services.GithubService;
import com.yalantis.interfaces.Manager;
import com.yalantis.model.Repository;

import java.io.IOException;
import java.util.List;

import io.realm.RealmObject;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiManager implements Manager {

    private GithubService mGithubService;
    private Retrofit mRetrofit;

    @Override
    public void init(Context context) {
        initRetrofit();
        initServices();
    }

    private void initRetrofit() {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
//                        .header(ApiSettings.HEADER_AUTH_TOKEN, getAuthToken())
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiSettings.SERVER)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(createGsonConverter())
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

    private void initServices() {
        mGithubService = mRetrofit.create(GithubService.class);
    }

    public Call<List<Repository>> getOrganizationRepos(@NonNull String organizationName, @NonNull String reposType) {
        return mGithubService.getOrganizationRepos(organizationName, reposType);
    }

    public Observable<List<Repository>> getOrganizationReposRx(@NonNull String organizationName, @NonNull String reposType) {
        return mGithubService.getOrganizationReposRx(organizationName, reposType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void clear() {

    }

}
