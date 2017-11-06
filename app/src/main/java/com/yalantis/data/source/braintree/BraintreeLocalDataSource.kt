package com.yalantis.data.source.braintree

import com.braintreepayments.api.models.PaymentMethodNonce
import com.yalantis.data.model.LastPaymentNonce
import com.yalantis.data.source.base.BaseLocalDataSource

/**
 * Created by jtsym on 11/6/2017.
 */
class BraintreeLocalDataSource : BaseLocalDataSource() {

    fun saveLastPaymentMethod(nonce: LastPaymentNonce) {
        realm.executeTransaction { realm -> realm.copyToRealmOrUpdate(nonce) }
    }

    fun getLastPaymentMethod(): PaymentMethodNonce? {
        return realm.where(LastPaymentNonce::class.java).findFirst().nonce
    }

}