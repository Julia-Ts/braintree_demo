package com.yalantis.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

public abstract class BaseMvpActivity<T extends BaseMvpPresenter> extends BaseActivity implements BaseMvpView {

    protected T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenterInstance();
        mPresenter.attachView(this);
    }

    protected @NonNull abstract T getPresenterInstance();

    @Override
    public Context getContext() {
        return this;
    }

}
