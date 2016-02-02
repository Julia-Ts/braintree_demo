package com.yalantis.api.task;

import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Base Api Task created for performing error handling in one place
 */
public abstract class ApiTask<E> implements Callback<E> {

    @Override
    public void onResponse(Response<E> response) {
        Timber.d("Success: %s", response.code());
        if (response.isSuccess()) {
            onSuccess(response.body());
        } else {
            handleApiError(response);
        }
    }

    /**
     * Callback for all child classes of {@link ApiTask}
     *
     * @param response which contains Object of type defined in child
     */
    protected abstract void onSuccess(E response);

    /**
     * Callback for all child classes of {@link ApiTask}*
     */
    protected abstract void onError();

    /**
     * Invoked when a network or unexpected exception occurred during the HTTP request.
     * Can be Override in child class for additional message throwing
     */
    @Override
    public void onFailure(Throwable t) {
        // TODO: Handle network/unexpected exceptions here.
    }

    /**
     * Base error handling method with original response
     *
     * @param response received in onResponse callback. Have code not 200. Have no body with
     *                 typed object defined in child. Have errorBody() and code() for handling error
     */
    public static void handleApiError(Response response) {
        // TODO: Parse & handle API errors here.
    }
}