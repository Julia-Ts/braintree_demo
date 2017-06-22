package com.yalantis.base;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.trello.navi2.Event;
import com.trello.navi2.rx.RxNavi;
import com.yalantis.manager.SharedPrefManager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by voltazor on 20/03/16.
 */
public abstract class BaseMvpPresenterImpl<V extends BaseMvpView> implements BaseMvpPresenter<V> {

    protected SharedPrefManager mSpManager;
    protected V mView;
    private CompositeDisposable mSubscriptionList = new CompositeDisposable();

    /**
     * Attach view to presenter, also here we have subscription
     * for destroy event. On destroy event we should detach view
     * and destroy presenter
     *
     * @param view extend BaseMvpView
     */
    @Override
    public void attachView(V view) {
        mView = view;
        mSpManager = SharedPrefManager.getInstance(view.getContext());
        mSubscriptionList.add(RxNavi.observe(view, Event.DESTROY).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Object object) throws Exception {
                detachView();
            }
        }));
    }


    /**
     * This method adds given rx disposable to the {@link #mSubscriptionList}
     * which is unsubscribed {@link #detachView()}
     *
     * @param disposable - rx disposable that must be unsubscribed {@link #detachView()}
     */
    protected void addDisposable(@NonNull Disposable disposable) {
        mSubscriptionList.add(disposable);
    }

    protected String getString(@StringRes int strResId) {
        return mView.getContext().getString(strResId);
    }

    protected String getString(@StringRes int strResId, Object... formatArgs) {
        return mView.getContext().getString(strResId, formatArgs);
    }

    /**
     * Here we are detaching view and removing and
     * unsubscribing all subscriptions
     */
    @Override
    public void detachView() {
        mSubscriptionList.clear();
        mView = null;
    }

}
