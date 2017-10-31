package com.yalantis.api

import android.support.annotation.StringRes
import android.text.TextUtils

import com.yalantis.R
import com.yalantis.api.body.ErrorBody
import com.yalantis.base.BaseView

import java.lang.ref.WeakReference
import java.net.SocketException
import java.net.UnknownHostException

import io.reactivex.functions.Consumer
import retrofit2.HttpException
import timber.log.Timber

/**
 * Created by voltazor on 17/06/16.
 */
class GeneralErrorHandler constructor(view: BaseView? = null, private val mShowError: Boolean = false, private val mCodeMessageMap: Map<Int, String>? = null, private val mFailureCallback: FailureCallback? = null) : Consumer<Throwable> {

    private val mViewReference: WeakReference<BaseView>

    constructor(view: BaseView, codeMessageMap: Map<Int, String>) : this(view, true, codeMessageMap, null) {}

    constructor(view: BaseView, codeMessageMap: Map<Int, String>, failureCallback: FailureCallback) : this(view, true, codeMessageMap, failureCallback) {}

    constructor(view: BaseView, failureCallback: FailureCallback) : this(view, true, null, failureCallback) {}

    constructor(view: BaseView, showError: Boolean, failureCallback: FailureCallback) : this(view, showError, null, failureCallback) {}

    init {
        mViewReference = WeakReference<BaseView>(view)
    }

    @Throws(Exception::class)
    override fun accept(throwable: Throwable) {
        Timber.e(throwable, throwable.message)
        var isNetwork = false
        var errorBody: ErrorBody? = null
        if (throwable is SocketException || throwable is UnknownHostException) {
            isNetwork = true
            showErrorIfRequired(R.string.internet_connection_unavailable)
        } else if (throwable is HttpException) {
            errorBody = ErrorBody.parseError(throwable.response())
            if (errorBody != null) {
                handleError(errorBody)
            }
        }

        mFailureCallback?.onFailure(throwable, errorBody, isNetwork)
    }

    private fun handleError(errorBody: ErrorBody) {
        if (mCodeMessageMap != null && mCodeMessageMap.containsKey(errorBody.code)) {
            showErrorIfRequired(mCodeMessageMap[errorBody.code])
            return
        }
        when (errorBody.code) {
            ErrorBody.INVALID_CREDENTIALS -> showErrorIfRequired(R.string.invalid_credentials)
            ErrorBody.EMAIL_IS_TAKEN -> showErrorIfRequired(R.string.email_is_taken)
        }
    }

    private fun showErrorIfRequired(@StringRes strResId: Int) {
        if (mShowError) {
            val view = mViewReference.get()
            view?.showError(strResId)
        }
    }

    private fun showErrorIfRequired(error: String?) {
        if (mShowError && !TextUtils.isEmpty(error)) {
            val view = mViewReference.get()
            view?.showError(error)
        }
    }

    interface FailureCallback {

        fun onFailure(throwable: Throwable, errorBody: ErrorBody?, isNetwork: Boolean)

    }

}
