package com.yalantis.api;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.yalantis.R;
import com.yalantis.api.body.ErrorBody;
import com.yalantis.base.BaseMvpView;

import java.lang.ref.WeakReference;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;
import timber.log.Timber;

/**
 * Created by voltazor on 17/06/16.
 */
public class GeneralErrorHandler implements Consumer<Throwable> {

    private final WeakReference<BaseMvpView> mViewReference;
    private final Map<Integer, String> mCodeMessageMap;
    private final FailureCallback mFailureCallback;
    private final boolean mShowError;

    public GeneralErrorHandler() {
        this(null, false, null, null);
    }

    public GeneralErrorHandler(BaseMvpView view) {
        this(view, false, null, null);
    }

    public GeneralErrorHandler(BaseMvpView view, boolean showError) {
        this(view, showError, null, null);
    }

    public GeneralErrorHandler(BaseMvpView view, Map<Integer, String> codeMessageMap) {
        this(view, true, codeMessageMap, null);
    }

    public GeneralErrorHandler(BaseMvpView view, Map<Integer, String> codeMessageMap, FailureCallback failureCallback) {
        this(view, true, codeMessageMap, failureCallback);
    }

    public GeneralErrorHandler(BaseMvpView view, FailureCallback failureCallback) {
        this(view, true, null, failureCallback);
    }

    public GeneralErrorHandler(BaseMvpView view, boolean showError, FailureCallback failureCallback) {
        this(view, showError, null, failureCallback);
    }

    public GeneralErrorHandler(BaseMvpView view, boolean showError, @Nullable Map<Integer, String> codeMessageMap, FailureCallback failureCallback) {
        mViewReference = new WeakReference<>(view);
        mFailureCallback = failureCallback;
        mCodeMessageMap = codeMessageMap;
        mShowError = showError;
    }

    @Override
    public void accept(Throwable throwable) {
        Timber.e(throwable, throwable.getMessage());
        boolean isNetwork = false;
        ErrorBody errorBody = null;
        if (throwable instanceof SocketException || throwable instanceof UnknownHostException) {
            isNetwork = true;
            showErrorIfRequired(R.string.internet_connection_unavailable);
        } else if (throwable instanceof HttpException) {
            errorBody = ErrorBody.parseError(((HttpException) throwable).response());
            if (errorBody != null) {
                handleError(errorBody);
            }
        }

        if (mFailureCallback != null) {
            mFailureCallback.onFailure(throwable, errorBody, isNetwork);
        }
    }

    private void handleError(ErrorBody errorBody) {
        if (mCodeMessageMap != null && mCodeMessageMap.containsKey(errorBody.getCode())) {
            showErrorIfRequired(mCodeMessageMap.get(errorBody.getCode()));
            return;
        }
        switch (errorBody.getCode()) {
            case ErrorBody.INVALID_CREDENTIALS:
                showErrorIfRequired(R.string.invalid_credentials);
                break;
            case ErrorBody.EMAIL_IS_TAKEN:
                showErrorIfRequired(R.string.email_is_taken);
                break;
        }
    }

    private void showErrorIfRequired(@StringRes int strResId) {
        if (mShowError) {
            BaseMvpView view = mViewReference.get();
            if (view != null) {
                view.showError(strResId);
            }
        }
    }

    private void showErrorIfRequired(String error) {
        if (mShowError && !TextUtils.isEmpty(error)) {
            BaseMvpView view = mViewReference.get();
            if (view != null) {
                view.showError(error);
            }
        }
    }

    public interface FailureCallback {

        void onFailure(Throwable throwable, @Nullable ErrorBody errorBody, boolean isNetwork);

    }

}
