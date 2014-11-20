package com.yalantis.manager;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.yalantis.Const;
import com.yalantis.api.ApiSettings;
import com.yalantis.api.MainExecutor;
import com.yalantis.api.request.UserApi;
import com.yalantis.api.task.GetUserTask;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidApacheClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public class ApiManager implements Manager {

    private Context context;
    private MainExecutor executor;
    private UserApi userApi;

    private RestAdapter restAdapter;

    @Override
    public void init(Context context) {
        this.context = context;
        initRestModules();
        this.executor = new MainExecutor();
        setApi();
    }

    private void initRestModules() {
        restAdapter = new RestAdapter.Builder()
                .setLogLevel(Const.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setEndpoint(ApiSettings.SERVER)
                .setClient(new AndroidApacheClient())
                .setConverter(createConverter()).setRequestInterceptor(createRequestInterceptor())
                .build();
    }

    public void getUser(String password) {
        executor.execute(new GetUserTask(userApi, password));
    }

    private RequestInterceptor createRequestInterceptor() {
        return new RequestInterceptor() {

            @Override
            public void intercept(RequestFacade request) {
                // request.addQueryParam(ApiSettings.API_KEY, ApiSettings.API_KEY_VALUE);
                // if (!TextUtils.isEmpty(App.dataManager.getToken())) {
                // request.addQueryParam(ApiSettings.AUTH_TOKEN, App.dataManager.getToken());
                // }
            }
        };
    }

    private Converter createConverter() {
        GsonBuilder builder = new GsonBuilder();
        return new GsonConverter(builder.create());
    }

    private void setApi() {
        this.userApi = restAdapter.create(UserApi.class);
    }

    @Override
    public void clear() {
        executor.clear();
    }

}
