package com.yalantis.contract;

import com.yalantis.base.BaseMvpView;

public interface BaseMvpPresenter<V extends BaseMvpView> {

    void attachView(V view);

    void detachView();

}
