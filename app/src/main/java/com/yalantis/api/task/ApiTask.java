package com.yalantis.api.task;

import com.google.gson.Gson;
import com.squareup.okhttp.ResponseBody;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import timber.log.Timber;

/**
 * Base Api Task created for performing error handling in one place
 */
public abstract class ApiTask<E> implements  Callback<E> {

    @Override
    public void onResponse(Response<E> response, Retrofit retrofit) {
        Timber.d("success", response.code());
        if (response.isSuccess()) {
            onSuccess(response.body());
        } else {
            handleFailure(response);
        }
    }

    /**
     * Invoked when a network or unexpected exception occurred during the HTTP request.
     * Can be Override in child class for additional message throwing
     */
    @Override
    public void onFailure(Throwable t) {
        Timber.d("failure: " + t.getMessage());
//        EventBus.getDefault().post(new ErrorApiEvent(t.getMessage(), true));
    }

    /**
     * Callback for all child classes of {@link ApiTask}
     *
     * @param response which contains Object of type defined in child
     */
    protected abstract void onSuccess(E response);

    /**
     * Base error handling method with original response
     *
     * @param response received in onResponse callback. Have code not 200. Have no body with
     *                 typed object defined in child. Have errorBody() and code() for handling error
     */
    public static void handleFailure(Response response) {
        // Error handling depends on server side response
//        ErrorResponse errorResponse = null;
//        try {
//            ResponseBody body = response.errorBody();
//            if (body != null && body.bytes() != null) {
//                String json = new String(body.bytes());
//                Gson gson = new Gson();
//                errorResponse = gson.fromJson(json, ErrorResponse.class);
//            }
//        } catch (Exception e) {
//            Timber.e("onFailure", e);
//        }
//
//        if (errorResponse == null) {
//            EventBus.getDefault().postSticky(new ErrorApiEvent(response.message(), false));
//        } else {
//            EventBus.getDefault().postSticky(new ErrorApiEvent(errorResponse.getErrors().getErrorMessage(), true));
//        }
    }
}