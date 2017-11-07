package com.yalantis.flow.braintree.sandbox

import com.braintreepayments.api.models.PaymentMethodNonce
import com.yalantis.base.BasePresenter
import com.yalantis.base.BaseView
import com.yalantis.data.model.CardNonceInfo
import com.yalantis.data.model.ClientToken
import com.yalantis.data.model.PayPalNonceInfo

/**
 * Created by jtsym on 11/2/2017.
 */
class BraintreeContract {

    interface Presenter : BasePresenter {

        fun getToken()

        fun createTransaction(nonce: String)

        fun saveLastPaymentAccountInfo(nonce: PaymentMethodNonce)

        fun getLastPayPalAccountInfo(): PayPalNonceInfo?

        fun getLastCardAccountInfo(): CardNonceInfo?
    }

    interface View : BaseView {

        fun onTokenReceived(token: ClientToken)

    }

}