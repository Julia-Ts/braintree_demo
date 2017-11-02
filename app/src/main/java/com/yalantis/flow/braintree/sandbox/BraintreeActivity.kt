package com.yalantis.flow.braintree.sandbox

import android.content.Context
import com.yalantis.R
import com.yalantis.base.BaseActivity
import android.content.Intent.getIntent
import android.view.View
import android.content.Intent.getIntent
import android.provider.DocumentsContract.EXTRA_ERROR
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import kotlinx.android.synthetic.main.activity_braintree.*
import timber.log.Timber
import com.braintreepayments.api.models.PaymentMethodNonce
import com.braintreepayments.api.dropin.utils.PaymentMethodType
import com.braintreepayments.api.exceptions.InvalidArgumentException
import com.braintreepayments.api.BraintreeFragment


/**
 * Created by jtsym on 11/2/2017.
 */
class BraintreeActivity : BaseActivity<BraintreeContract.Presenter>(), BraintreeContract.View {

    private val REQUEST_BRAINTREE = 8391

    override val presenter: BraintreeContract.Presenter = BraintreePresenter()
    override val layoutResourceId: Int = R.layout.activity_braintree

    private lateinit var token: String
    private lateinit var mBraintreeFragment: BraintreeFragment

    override fun getContext(): Context = this@BraintreeActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getToken()
        payBtn.setOnClickListener({ checkPreviousPaymentMethods() })
    }

    private fun getToken() {
        //It should be replaced with real token from server. This one is only for testing purposes
        token = getString(R.string.mock_token)
    }

    //This functionality will work only if token on the server is created with using customer_id
    //https://developers.braintreepayments.com/reference/request/client-token/generate/ruby#customer_id
    private fun checkPreviousPaymentMethods() {
        DropInResult.fetchDropInResult(this, token, object : DropInResult.DropInResultListener {
            override fun onError(exception: Exception) {
                Timber.e("TESTING", "error while checking prev payments " + exception.message)
                onBraintreeSubmit()
            }

            override fun onResult(result: DropInResult) {
                if (result.paymentMethodType != null) {
                    // use the icon and name to show in your UI
                    val icon = result.paymentMethodType?.drawable
                    val name = result.paymentMethodType?.localizedName

                    icon?.let {
                        paymentIcon.setImageResource(it)
                    }
                    name?.let {
                        paymentTitle.setText(it)
                    }

                    if (result.paymentMethodType == PaymentMethodType.ANDROID_PAY) {
                        // The last payment method the user used was Android Pay.
                        // The Android Pay flow will need to be performed by the
                        // user again at the time of checkout.
                    } else {
                        // Use the payment method show in your UI and charge the user
                        // at the time of checkout.
                        val paymentMethod = result.paymentMethodNonce
                    }
                } else {
                    onBraintreeSubmit()
                }
            }
        })
    }

    private fun onBraintreeSubmit() {
        val dropInRequest = DropInRequest().clientToken(token)
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_BRAINTREE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_BRAINTREE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    // use the result to update your UI and send the payment method nonce to your server
                    val result: DropInResult? = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT)
                    Timber.d(javaClass.simpleName, "payment result: " + result?.deviceData)
                }
                Activity.RESULT_CANCELED -> {
                    // the user canceled
                    Timber.e(javaClass.simpleName, "payment canceled by user")
                }
                else -> {
                    // handle errors here, an exception may be available in
                    val error = data.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception?
                    Timber.e(javaClass.simpleName, "payment error " + error?.message)
                }
            }
        }
    }

    private fun prepareBraintreeFragment() {
        try {
            mBraintreeFragment = BraintreeFragment.newInstance(this, token)
            // mBraintreeFragment is ready to use!
        } catch (e: InvalidArgumentException) {
            // There was an issue with your authorization string.
        }
    }


}