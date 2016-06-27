package com.yalantis.contract;

/**
 * Created by voltazor on 27/06/16.
 */
public final class SomeContract {

    public interface Presenter extends BaseMvpPresenter<View> {

        void doSomeAction();

    }

    public interface View extends BaseMvpView {

        void showSomeResult();

    }

}
