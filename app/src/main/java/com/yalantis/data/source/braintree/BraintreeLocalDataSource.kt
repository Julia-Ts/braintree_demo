package com.yalantis.data.source.braintree

import com.yalantis.data.model.CardNonceInfo
import com.yalantis.data.model.PayPalNonceInfo
import com.yalantis.data.source.base.BaseLocalDataSource

/**
 * Created by jtsym on 11/6/2017.
 */
class BraintreeLocalDataSource : BaseLocalDataSource() {

    fun saveLastPayPalAccountInfo(nonce: PayPalNonceInfo) {
        realm.executeTransaction { realm -> realm.copyToRealmOrUpdate(nonce) }
    }

    fun saveLastCardAccountInfo(nonce: CardNonceInfo) {
        realm.executeTransaction { realm -> realm.copyToRealmOrUpdate(nonce) }
    }

    fun getLastCardAccountInfo(): CardNonceInfo? {
        return realm.where(CardNonceInfo::class.java).findFirst()
    }

    fun getLastPayPalAccountInfo(): PayPalNonceInfo? {
        return realm.where(PayPalNonceInfo::class.java).findFirst()
    }

}