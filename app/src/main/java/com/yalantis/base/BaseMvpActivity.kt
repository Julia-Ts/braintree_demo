package com.yalantis.base

import android.os.Bundle

@Suppress("UNCHECKED_CAST", "LeakingThis")
abstract class BaseMvpActivity<in V : BaseMvpView, out T : BaseMvpPresenter<V>> : BaseActivity(), BaseMvpView {

    protected val mPresenter: T = presenterInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this as V)
    }

    abstract fun presenterInstance(): T

}
