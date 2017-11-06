package com.yalantis.data.source.braintree

import com.yalantis.data.model.Transaction
import com.yalantis.data.source.base.BaseRemoteDataSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by jtsym on 11/6/2017.
 */
class BraintreeDataSource : BaseRemoteDataSource() {

    fun createTransaction(nonce: String): Single<Transaction> {
        return braintreeService.createTransaction(nonce)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    init {
        init()
    }

}