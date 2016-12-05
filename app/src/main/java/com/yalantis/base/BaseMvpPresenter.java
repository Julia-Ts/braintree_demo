package com.yalantis.base;

public interface BaseMvpPresenter<V extends BaseMvpView> {

    void attachView(V view);

    void detachView();

}
