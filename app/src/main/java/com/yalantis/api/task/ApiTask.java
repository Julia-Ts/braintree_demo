package com.yalantis.api.task;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.yalantis.App;
import com.yalantis.api.QueuedExecutorCallback;
import com.yalantis.event.ErrorApiEvent;
import com.yalantis.model.ApiError;
import com.yalantis.model.dto.BaseDTO;
import com.yalantis.model.dto.ErrorResponse;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import timber.log.Timber;

/**
 * Created by: Dmitriy Dovbnya
 * Date: 21.09.13 19:34
 */
public abstract class ApiTask<T, E extends BaseDTO> implements Runnable, Callback<E> {

    public static final int CODE_INVALID_TOKEN = -20;
    protected QueuedExecutorCallback callback;
    protected T api;
    protected String apiKey;

    protected ApiTask(T api) {
        this.api = api;
        this.apiKey = null;
    }

    protected ApiTask(T api, String apiKey) {
        this.api = api;
        this.apiKey = apiKey;
    }

    public void cancel() {
    }

    @Override
    public void success(E e, Response response) {
        if (e != null) {
            int code = e.getStatus();
            switch (code) {
            case 0:
                onSuccess(e, response);
                break;
            case CODE_INVALID_TOKEN:
                parseError(e, response);
                break;
            default:
                parseError(e, response);
                break;
            }

        }
        finished();
    }

    protected void parseError(E e, Response response) {
        String message = e.getMessage();
        if (!TextUtils.isEmpty(e.getMessage())) {
            Timber.e(e.getMessage());
        }
        ApiError error = ApiError.fromCode(e.getStatus());

        switch (e.getMethod()) {

        default:
            if (error != null) {
                App.eventBus.postSticky(new ErrorApiEvent(error));
            } else {
                App.eventBus.postSticky(new ErrorApiEvent(message, true));
            }
            break;
        }

    }

    @Override
    public void failure(RetrofitError error) {
        Timber.d("Failure", error);
        onFailure(error);
        finished();
    }

    /**
     * @param callback
     *            for ApiTaskExecutor
     */
    public void setCallback(QueuedExecutorCallback callback) {
        this.callback = callback;
    }

    public void finished() {
        if (callback != null) {
            callback.finished();
        }
    }

    public abstract void onSuccess(E e, Response response);

    protected void onFailure(RetrofitError error) {
        ErrorResponse errorResponse = null;
        try {
            Response response = error.getResponse();
            if (response != null) {
                TypedByteArray body = (TypedByteArray) response.getBody();
                if (body != null && body.getBytes() != null) {
                    String json = new String(body.getBytes());
                    Gson gson = new Gson();
                    errorResponse = gson.fromJson(json, ErrorResponse.class);
                }
            }
        } catch (Exception e) {

        }
        if (errorResponse == null && error.isNetworkError()) {
            EventBus.getDefault().postSticky(new ErrorApiEvent(ApiError.NO_INTERNET));
        }
        if (errorResponse == null) {
            EventBus.getDefault().postSticky(new ErrorApiEvent(error.getMessage(), false));
        } else {
            EventBus.getDefault().postSticky(new ErrorApiEvent(errorResponse.getErrorMessage(), true));
        }
    }

}