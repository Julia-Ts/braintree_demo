package com.yalantis.contract;

public interface BaseMvpPresenter<V extends BaseMvpView> {

    void attachView(V view);

    void detachView();

}
