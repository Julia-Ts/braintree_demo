package com.yalantis.base

import android.support.annotation.StringRes
import com.yalantis.manager.SharedPrefManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by voltazor on 20/03/16.
 */
abstract class BasePresenterImplementation<V : BaseView> : BasePresenter {

    protected var view: V? = null
    private val compositeDisposable = CompositeDisposable()

    /**
     * Attach view to presenter, also here we have subscription
     * for destroy event. On destroy event we should detach view
     * and destroy presenter

     * @param view extend BaseView
     */
    @Suppress("UNCHECKED_CAST")
    override fun attachView(view: BaseView) {
        this.view = view as V
        SharedPrefManager.init(view.getContext())
    }

    /**
     * This method adds given rx subscription to the [.mSubscriptionList]
     * which is unsubscribed [.detachView]

     * @param subscription - rx subscription that must be unsubscribed [.detachView]
     */
    protected fun addDisposable(subscription: Disposable) {
        compositeDisposable.add(subscription)
    }

    protected fun getString(@StringRes strResId: Int): String? {
        return view?.getContext()?.getString(strResId)
    }

    protected fun getString(@StringRes strResId: Int, vararg formatArgs: Any): String? {
        return view?.getContext()?.getString(strResId, *formatArgs)
    }

    /**
     * Here we are detaching view and removing and
     * unsubscribing all subscriptions
     */
    override fun detachView() {
        compositeDisposable.dispose()
        view = null
    }

}
