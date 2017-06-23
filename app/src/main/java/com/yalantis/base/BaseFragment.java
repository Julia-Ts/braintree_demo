package com.yalantis.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.navi2.component.support.NaviFragment;
import com.yalantis.interfaces.BaseActivityCallback;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends NaviFragment {

    private BaseActivityCallback mBaseActivityCallback;
    private Unbinder mBind;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mBaseActivityCallback = (BaseActivityCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.getClass().getSimpleName() + " must implement" + BaseActivityCallback.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(getLayoutResourceId(), container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    protected abstract int getLayoutResourceId();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBaseActivityCallback = null;
    }

    public void showProgress() {
        mBaseActivityCallback.showProgress();
    }

    public void hideProgress() {
        mBaseActivityCallback.hideProgress();
    }

    public void showError(String message) {
        mBaseActivityCallback.showError(message);
    }

    public void showError(@StringRes int strResId) {
        mBaseActivityCallback.showError(strResId);
    }

    public void hideKeyboard() {
        mBaseActivityCallback.hideKeyboard();
    }

    public void showMessage(String message) {
        mBaseActivityCallback.showMessage(message);
    }

    public void showMessage(@StringRes int srtResId) {
        mBaseActivityCallback.showMessage(srtResId);
    }

    public abstract String getFragmentTag();

}
