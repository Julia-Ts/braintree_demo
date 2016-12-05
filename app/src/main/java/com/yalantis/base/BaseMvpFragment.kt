package com.yalantis.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


abstract class BaseMvpFragment<in V: BaseMvpView, T : BaseMvpPresenter<V>> : BaseFragment(), BaseMvpView {

    abstract fun presenterInstance(): T

    protected var mPresenter: T = presenterInstance()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        mPresenter.attachView(this as V)
        return view
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

}
