package com.yalantis.base

import android.support.annotation.StringRes
import com.trello.navi.Event
import com.trello.navi.rx.RxNavi
import com.yalantis.manager.SharedPrefManager
import rx.Subscription
import rx.internal.util.SubscriptionList

/**
 * Created by voltazor on 20/03/16.
 */
abstract class BaseMvpPresenterImpl<V : BaseMvpView> : BaseMvpPresenter<V> {

    protected lateinit var mSpManager: SharedPrefManager
    protected var mView: V? = null
    private val mSubscriptionList = SubscriptionList()

    /**
     * Attach view to presenter, also here we have subscription
     * for destroy event. On destroy event we should detach view
     * and destroy presenter

     * @param view extend BaseMvpView
     */
    override fun attachView(view: V) {
        mView = view
        mSpManager = SharedPrefManager.getInstance(view.getContext())
        mSubscriptionList.add(RxNavi.observe(view, Event.DESTROY).subscribe { detachView() })
    }

    /**
     * This method adds given rx subscription to the [.mSubscriptionList]
     * which is unsubscribed [.detachView]

     * @param subscription - rx subscription that must be unsubscribed [.detachView]
     */
    protected fun addSubscription(subscription: Subscription) {
        mSubscriptionList.add(subscription)
    }

    protected fun getString(@StringRes strResId: Int): String? {
        return mView?.getContext()?.getString(strResId)
    }

    protected fun getString(@StringRes strResId: Int, vararg formatArgs: Any): String? {
        return mView?.getContext()?.getString(strResId, *formatArgs)
    }

    /**
     * Here we are detaching view and removing and
     * unsubscribing all subscriptions
     */
    override fun detachView() {
        mSubscriptionList.unsubscribe()
        mSubscriptionList.clear()
        mView = null
    }

}
