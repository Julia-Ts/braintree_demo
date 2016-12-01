package com.yalantis.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yalantis.contract.BaseMvpPresenter;


public abstract class BaseMvpFragment<T extends BaseMvpPresenter> extends BaseFragment implements BaseMvpView {

    protected T mPresenter;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mPresenter = getPresenterInstance();
        mPresenter.attachView(this);
        return view;
    }

    protected @NonNull abstract T getPresenterInstance();

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }

}
