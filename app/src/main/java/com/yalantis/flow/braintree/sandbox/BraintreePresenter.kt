package com.yalantis.flow.braintree.sandbox

import android.text.TextUtils
import com.braintreepayments.api.models.CardNonce
import com.braintreepayments.api.models.PayPalAccountNonce
import com.braintreepayments.api.models.PaymentMethodNonce
import com.yalantis.base.BasePresenterImplementation
import com.yalantis.data.model.CardNonceInfo
import com.yalantis.data.model.PayPalNonceInfo
import com.yalantis.data.source.braintree.BraintreeRepository
import timber.log.Timber

/**
 * Created by jtsym on 11/2/2017.
 */
class BraintreePresenter : BasePresenterImplementation<BraintreeContract.View>(), BraintreeContract.Presenter {

    private val repo = BraintreeRepository()

    override fun getToken() {
        addDisposable(repo.getToken().subscribe({
            view?.onTokenReceived(it)
        }, {
            Timber.e(it)
        }))
    }

    override fun createTransaction(nonce: String) {
        addDisposable(repo.createTransaction(nonce)
                .subscribe({
                    Timber.d(">>> Response got")
                    //This logic was taken from example of Braintree Drop-ui.
                    //https://github.com/braintree/braintree-android-drop-in
                    //https://github.com/braintree/braintree_spring_example
                    val message = it.getMessage()
                    if (message != null && message.startsWith("created")) { //I hope you will have to deal with better backend response
                        view?.showMessage("Transaction was created successfully")
                        Timber.d(">>> Transaction was created successfully")
                    } else {
                        if (TextUtils.isEmpty(it.getMessage())) {
                            view?.showError("Server response was empty or malformed")
                            Timber.e(">>> Server response was empty or malformed")
                        } else {
                            view?.showError(it.getMessage())
                            Timber.e(">>> " + it.getMessage())
                        }
                    }
                }, {
                    view?.showError(it?.message)
                    Timber.e(it)
                }))
    }

    override fun saveLastPaymentAccountInfo(nonce: PaymentMethodNonce) {
        when (nonce) {
            is PayPalAccountNonce -> repo.saveLastPayPalAccountInfo(PayPalNonceInfo(1, nonce.email, nonce.billingAddress.extendedAddress))
            is CardNonce -> repo.saveLastCardAccountInfo(CardNonceInfo(1, nonce.lastTwo))
        }
    }

    override fun getLastPayPalAccountInfo(): PayPalNonceInfo? {
        return repo.getLastPayPalAccountInfo()
    }

    override fun getLastCardAccountInfo(): CardNonceInfo? {
        return repo.getLastCardAccountInfo()
    }

}