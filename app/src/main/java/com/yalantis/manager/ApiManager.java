package com.yalantis.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.yalantis.api.ApiSettings;
import com.yalantis.api.services.GithubService;
import com.yalantis.interfaces.Manager;
import com.yalantis.model.GithubRepository;

import java.io.IOException;
import java.util.List;

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
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    private void initServices() {
        mGithubService = mRetrofit.create(GithubService.class);
    }

    public Call<List<GithubRepository>> getOrganizationRepos(@NonNull String organizationName, @NonNull String reposType) {
        return mGithubService.getOrganizationRepos(organizationName, reposType);
    }

    public Observable<List<GithubRepository>> getOrganizationReposRx(@NonNull String organizationName, @NonNull String reposType) {
        return mGithubService.getOrganizationReposRx(organizationName, reposType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void clear() {

    }

}
