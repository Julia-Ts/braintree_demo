package com.yalantis.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.trello.navi.component.support.NaviFragment
import com.yalantis.interfaces.BaseActivityCallback

abstract class BaseFragment : NaviFragment() {

    private var mBaseActivityCallback: BaseActivityCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mBaseActivityCallback = context as BaseActivityCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(context.javaClass.getSimpleName() + " must implement" + BaseActivityCallback::class.java.simpleName)
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(layoutResourceId(), container, false)
        ButterKnife.bind(this, view)
        return view
    }

    abstract fun layoutResourceId(): Int

    override fun onDestroyView() {
        super.onDestroyView()
        ButterKnife.unbind(this)
    }

    override fun onDetach() {
        super.onDetach()
        mBaseActivityCallback = null
    }

    fun showProgress() {
        mBaseActivityCallback!!.showProgress()
    }

    fun hideProgress() {
        mBaseActivityCallback!!.hideProgress()
    }

    fun showError(message: String) {
        mBaseActivityCallback!!.showError(message)
    }

    fun showError(@StringRes strResId: Int) {
        mBaseActivityCallback!!.showError(strResId)
    }

    fun hideKeyboard() {
        mBaseActivityCallback!!.hideKeyboard()
    }

    fun showMessage(message: String) {
        mBaseActivityCallback!!.showMessage(message)
    }

    fun showMessage(@StringRes srtResId: Int) {
        mBaseActivityCallback!!.showMessage(srtResId)
    }

    abstract val fragmentTag: String

}
