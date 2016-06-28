package com.yalantis.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.yalantis.contract.BaseMvpPresenter;
import com.yalantis.contract.BaseMvpView;

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
