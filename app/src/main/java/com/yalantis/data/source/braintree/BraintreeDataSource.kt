package com.yalantis.data.source.braintree

import com.yalantis.data.model.Transaction
import com.yalantis.data.source.base.BaseRemoteDataSource
import retrofit2.Callback

/**
 * Created by jtsym on 11/6/2017.
 */
class BraintreeDataSource : BaseRemoteDataSource() {

    fun createTransaction(nonce: String, callback: Callback<Transaction>) {
        braintreeService.createTransaction(nonce, callback)
    }

}