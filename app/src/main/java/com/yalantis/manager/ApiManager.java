package com.yalantis.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.yalantis.api.ApiSettings;
import com.yalantis.api.services.GithubService;
import com.yalantis.interfaces.Manager;
import com.yalantis.model.GithubRepo;

import java.io.IOException;
import java.util.List;

import retrofit.Callback;
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

    /**
     * Initialization of Retrofit (rest adapter)
     * Setting updated HttpClient if needed
     */
    private void initRetrofit() {
        // Custom Client need only in cases if some header data ot something else - changed
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        /*
                        .header(ApiSettings.HEADER_AUTH_TOKEN,
                                ApiSettings.AUTH_TOKEN_PREFIX + App.getAccountManager().getAuthToken())
                                */
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });

        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiSettings.SERVER)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        // Names can be changed in release version
                .addConverterFactory(GsonConverterFactory.create())
                        // Client should be added in case if any changes are present
                        // By default each Adapter have is own client
                /*
                .client(client)
                */
                .build();
    }


    /**
     * Initialize of Retrofit services with requests to server
     */
    private void initServices() {
        mGithubService = mRetrofit.create(GithubService.class);
    }

    public void getOrganizationRepos(@NonNull String organizationName, @NonNull String reposType, Callback<List<GithubRepo>> callback) {
        mGithubService.getOrganizationRepos(organizationName, reposType).enqueue(callback);
    }

    public Observable<List<GithubRepo>> getOrganizationReposRx(@NonNull String organizationName, @NonNull String reposType) {
        return mGithubService.getOrganizationReposRx(organizationName, reposType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public void clear() {

    }
}
