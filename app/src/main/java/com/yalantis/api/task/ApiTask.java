package com.yalantis.api.task;

import com.google.gson.Gson;
import com.squareup.okhttp.ResponseBody;
import com.yalantis.event.ErrorApiEvent;
import com.yalantis.interfaces.QueuedExecutorCallback;
import com.yalantis.model.dto.ErrorResponse;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import timber.log.Timber;

/**
 * Base Api Task created for performing error handling in one place
 */
public abstract class ApiTask<T, E> implements Runnable, Callback<E> {

    protected QueuedExecutorCallback callback;
    protected T api;
    protected String apiKey;

    protected ApiTask(T api) {
        this.api = api;
        this.apiKey = null;
    }

    @Override
    public void onResponse(Response<E> response, Retrofit retrofit) {
        Timber.d("success", response.code());
        if (response.isSuccess()) {
            onSuccess(response);
        } else {
            handleFailure(response);
        }
        finished();
    }

    /**
     * Invoked when a network or unexpected exception occurred during the HTTP request.
     * Can be Overriten in child class for additional message throwing
     */
    @Override
    public void onFailure(Throwable t) {
        Timber.d("failure: " + t.getMessage());
        EventBus.getDefault().post(new ErrorApiEvent(t.getMessage(), true));
        finished();
    }

    /**
     * @param callback for ApiTaskExecutor
     */
    public void setCallback(QueuedExecutorCallback callback) {
        this.callback = callback;
    }

    public void finished() {
        if (callback != null) {
            callback.finished();
        }
    }

    /**
     * Callback for all child classes of {@link ApiTask}
     *
     * @param response which contains Object of type defined in child
     */
    protected abstract void onSuccess(Response response);

    /**
     * TODO: need to fill for concrete project, depends on server side
     * Base error handling method with original response
     *
     * @param response received in onResponse callback. Have code not 200. Have no body with
     *                 typed object defined in child. Have errorBody() and code() for handling error
     */
    public static void handleFailure(Response response) {
        ErrorResponse errorResponse = null;
        // Generally we can use response.code() for specifying error
        switch (response.code()) {
            case 400:
            case 403:
            case 404:
                break;
        }
        // Error handling depends on server side response
        try {
            ResponseBody body = response.errorBody();
            if (body != null && body.bytes() != null) {
                String json = new String(body.bytes());
                Gson gson = new Gson();
                errorResponse = gson.fromJson(json, ErrorResponse.class);
            }
        } catch (Exception e) {
            Timber.e("onFailure", e);
        }

        // Message display for case of unhandled error
        if (errorResponse == null) {
            EventBus.getDefault().postSticky(new ErrorApiEvent(response.message(), false));
        } else {
            EventBus.getDefault().postSticky(new ErrorApiEvent(errorResponse.getErrorMessage(), true));
        }
    }
}