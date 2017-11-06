package com.yalantis.data.source.braintree

import com.braintreepayments.api.models.PaymentMethodNonce
import com.yalantis.data.model.LastPaymentNonce
import com.yalantis.data.model.Transaction
import io.reactivex.Single

/**
 * Created by jtsym on 11/6/2017.
 */
class BraintreeRepository {

    private val remoteSource = BraintreeRemoteDataSource().apply { init() }
    private val localSource = BraintreeLocalDataSource().apply { init() }

    fun createTransaction(nonce: String): Single<Transaction> {
        return remoteSource.createTransaction(nonce)
    }

    fun saveLastPaymentMethod(nonce: LastPaymentNonce) {
        localSource.saveLastPaymentMethod(nonce)
    }

    fun getLastPaymentMethod(): PaymentMethodNonce? {
        return localSource.getLastPaymentMethod()
    }

}