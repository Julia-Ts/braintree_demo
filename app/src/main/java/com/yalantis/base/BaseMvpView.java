package com.yalantis.base;

import android.content.Context;
import android.support.annotation.StringRes;

import com.trello.navi.NaviComponent;

public interface BaseMvpView extends NaviComponent {

    Context getContext();

    void showProgress();

    void hideProgress();

    void showError(@StringRes int strResId);

    void showError(String error);

    void showMessage(@StringRes int srtResId);

    void showMessage(String message);

}
