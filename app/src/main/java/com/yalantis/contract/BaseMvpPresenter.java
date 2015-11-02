package com.yalantis.contract;

public interface BaseMvpPresenter<V> {

    void attachView(V view);

    void detachView();

}
