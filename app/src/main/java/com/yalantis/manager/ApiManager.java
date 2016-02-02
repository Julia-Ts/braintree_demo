package com.yalantis.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.yalantis.api.ApiSettings;
import com.yalantis.api.serializer.RepositorySerializer;
import com.yalantis.api.services.GithubService;
import com.yalantis.interfaces.Manager;
import com.yalantis.model.Repository;

import java.io.IOException;
import java.util.List;

import io.realm.RealmObject;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

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
            public Response intercept(Chain chain) throws IOException {
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

    @Override
    public void clear() {

    }

}
