package com.yalantis.flow.braintree.sandbox

import android.text.TextUtils
import com.yalantis.base.BasePresenterImplementation
import com.yalantis.data.source.braintree.BraintreeDataSource
import timber.log.Timber

/**
 * Created by jtsym on 11/2/2017.
 */
class BraintreePresenter : BasePresenterImplementation<BraintreeContract.View>(), BraintreeContract.Presenter {

    private val braintreeDataSource = BraintreeDataSource()

    override fun createTransaction(nonce: String) {
        addDisposable(braintreeDataSource.createTransaction(nonce)
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

}