package com.yalantis.manager;

import android.content.Context;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.yalantis.api.ApiSettings;
import com.yalantis.api.MainExecutor;
import com.yalantis.api.rx.LoginRequest;
import com.yalantis.api.services.TokenLessService;
import com.yalantis.api.task.LoginTask;
import com.yalantis.interfaces.CallbackListener;
import com.yalantis.interfaces.LoginListener;
import com.yalantis.interfaces.Manager;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public class ApiManager implements Manager {

    private MainExecutor mExecutor;
    private TokenLessService mTokenLessService;
    private Retrofit mTokenLessRetrofit;

    @Override
    public void init(Context context) {
        // TODO: remove braces when URL will be added
        // initRetrofit();
        // initServices();
        mExecutor = new MainExecutor();
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
                // Customize the request
                Request request = original.newBuilder()
                        /*
                        .header(ApiSettings.HEADER_AUTH_TOKEN,
                                ApiSettings.AUTH_TOKEN_PREFIX + App.getAccountManager().getAuthToken())
                                */
                        .method(original.method(), original.body())
                        .build();
                // Customize or return the response
                return chain.proceed(request);
            }
        });

        mTokenLessRetrofit = new Retrofit.Builder()
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
        mTokenLessService = mTokenLessRetrofit.create(TokenLessService.class);
    }

    /**
     * Login task with async request execution example
     *
     * @param listener of request execution status from outside
     */
    public void login(String email, String password, final CallbackListener listener) {
        mExecutor.execute(new LoginTask(mTokenLessService, email, password, listener));
    }

    /**
     * Login request with RxJava execution example
     *
     * @param listener of request execution status from outside
     */
    public void loginRX(String email, String password, LoginListener listener) {
        new LoginRequest(mTokenLessService, email, password, listener).login();
    }

    @Override
    public void clear() {
        mExecutor.clear();
    }
}
