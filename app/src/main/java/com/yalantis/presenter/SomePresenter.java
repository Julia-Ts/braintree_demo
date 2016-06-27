package com.yalantis.presenter;

import android.support.annotation.Nullable;

import com.yalantis.api.GeneralErrorHandler;
import com.yalantis.api.body.ErrorBody;
import com.yalantis.contract.SomeContract;
import com.yalantis.model.SomeModel;

import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by voltazor on 27/06/16.
 */
public class SomePresenter extends BaseMvpPresenterImpl<SomeContract.View> implements SomeContract.Presenter {

    @Override
    public void doSomeAction() {
        mView.showProgress();
        addSubscription(mApiManager.someApiCall().subscribe(new Action1<SomeModel>() {
            @Override
            public void call(SomeModel model) {
                mView.hideProgress();
                mView.showSomeResult();
            }
        }, new GeneralErrorHandler(mView, new GeneralErrorHandler.FailureCallback() {
            @Override
            public void onFailure(Throwable throwable, @Nullable ErrorBody errorBody, boolean isNetwork) {
                mView.hideProgress();
                if (!isNetwork && errorBody != null) {
                    Timber.e(throwable, errorBody.toString());
                }
            }
        })));
    }

}
