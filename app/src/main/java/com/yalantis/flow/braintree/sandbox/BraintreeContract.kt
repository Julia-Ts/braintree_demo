package com.yalantis.flow.braintree.sandbox

import com.braintreepayments.api.models.PaymentMethodNonce
import com.yalantis.base.BasePresenter
import com.yalantis.base.BaseView

/**
 * Created by jtsym on 11/2/2017.
 */
class BraintreeContract {

    interface Presenter : BasePresenter {

        fun createTransaction(nonce: String)

        fun saveLastPaymentMethod(nonce: PaymentMethodNonce)

        fun getLastPaymentMethod(): PaymentMethodNonce?

    }

    interface View : BaseView

}