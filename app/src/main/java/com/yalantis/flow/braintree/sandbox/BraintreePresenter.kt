package com.yalantis.flow.braintree.sandbox

import com.yalantis.base.BasePresenterImplementation
import com.yalantis.data.model.Transaction
import com.yalantis.data.source.braintree.BraintreeDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * Created by jtsym on 11/2/2017.
 */
class BraintreePresenter : BasePresenterImplementation<BraintreeContract.View>(), BraintreeContract.Presenter {

    private val braintreeDataSource = BraintreeDataSource()

    override fun createTransaction(nonce: String) {
        braintreeDataSource.createTransaction(nonce, object : Callback<Transaction> {
            override fun onFailure(call: Call<Transaction>?, t: Throwable?) {
                view?.showError(t?.message)
                Timber.e(t)
            }

            override fun onResponse(call: Call<Transaction>?, response: Response<Transaction>?) {
                Timber.e("Response got")
                if (response?.isSuccessful != null && response.isSuccessful) {
                    Timber.e("Response successful", response.body().toString())
//                    if (transaction.getMessage() != null && transaction.getMessage().startsWith("created")) {
//                        setStatus(R.string.transaction_complete)
//                        setMessage(transaction.getMessage())
//                    } else {
//                        setStatus(R.string.transaction_failed)
//                        if (TextUtils.isEmpty(transaction.getMessage())) {
//                            setMessage("Server response was empty or malformed")
//                        } else {
//                            setMessage(transaction.getMessage())
//                        }
//                    }
                } else {
                    Timber.e("Response failed", response?.errorBody())
                }
            }
        })
    }

}