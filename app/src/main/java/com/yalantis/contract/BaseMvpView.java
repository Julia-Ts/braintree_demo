package com.yalantis.contract;

import android.content.Context;
import android.support.annotation.StringRes;

public interface BaseMvpView {

    Context getContext();

    void showProgress();

    void hideProgress();

    void showError(@StringRes int strResId);

    void showError(String error);

    void showMessage(@StringRes int srtResId);

    void showMessage(String message);

}
