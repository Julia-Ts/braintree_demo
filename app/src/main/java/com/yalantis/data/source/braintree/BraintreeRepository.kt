package com.yalantis.data.source.braintree

import com.yalantis.data.model.CardNonceInfo
import com.yalantis.data.model.PayPalNonceInfo
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

    fun saveLastPayPalAccountInfo(nonce: PayPalNonceInfo) {
        localSource.saveLastPayPalAccountInfo(nonce)
    }

    fun saveLastCardAccountInfo(nonce: CardNonceInfo) {
        localSource.saveLastCardAccountInfo(nonce)
    }

    fun getLastPayPalAccountInfo(): PayPalNonceInfo? {
        return localSource.getLastPayPalAccountInfo()
    }

    fun getLastCardAccountInfo(): CardNonceInfo? {
        return localSource.getLastCardAccountInfo()
    }

}