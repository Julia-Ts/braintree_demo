package com.yalantis.flow.braintree.sandbox

import android.text.TextUtils
import com.braintreepayments.api.models.PaymentMethodNonce
import com.yalantis.base.BasePresenterImplementation
import com.yalantis.data.model.LastPaymentNonce
import com.yalantis.data.source.braintree.BraintreeRemoteDataSource
import com.yalantis.data.source.braintree.BraintreeRepository
import timber.log.Timber

/**
 * Created by jtsym on 11/2/2017.
 */
class BraintreePresenter : BasePresenterImplementation<BraintreeContract.View>(), BraintreeContract.Presenter {

    private val repo = BraintreeRepository()

    override fun createTransaction(nonce: String) {
        addDisposable(repo.createTransaction(nonce)
                .subscribe({
                    Timber.d(">>> Response got")
                    //This logic was in an example of Braintree Drop-ui.
                    //https@ //github.com/braintree/braintree-android-drop-in
                    //So their server in example sends such response and we have to deal with it
                    //https://github.com/braintree/braintree_spring_example
                    val message = it.getMessage()
                    if (message != null && message.startsWith("created")) {
                        Timber.d(">>> Transaction was created successfully")
                    } else {
                        if (TextUtils.isEmpty(it.getMessage())) {
                            Timber.e(">>> Server response was empty or malformed")
                        } else {
                            Timber.e(">>> " + it.getMessage())
                        }
                    }
                }, {
                    Timber.e(it)
                }))
    }

    override fun saveLastPaymentMethod(nonce: PaymentMethodNonce) {
        repo.saveLastPaymentMethod(LastPaymentNonce(nonce))
    }

    override fun getLastPaymentMethod(): PaymentMethodNonce? {
        return repo.getLastPaymentMethod()
    }

}