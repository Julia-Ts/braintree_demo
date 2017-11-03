package com.yalantis.flow.braintree.sandbox

import android.content.Context
import com.yalantis.R
import com.yalantis.base.BaseActivity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import kotlinx.android.synthetic.main.activity_braintree.*
import timber.log.Timber
import com.braintreepayments.api.models.PaymentMethodNonce
import com.braintreepayments.api.dropin.utils.PaymentMethodType
import com.braintreepayments.api.exceptions.InvalidArgumentException
import com.braintreepayments.api.BraintreeFragment
import com.braintreepayments.api.Card
import com.braintreepayments.api.models.CardNonce
import com.braintreepayments.api.models.PayPalAccountNonce
import com.braintreepayments.api.PayPal
import com.braintreepayments.api.models.PayPalRequest

/**
 * Created by jtsym on 11/2/2017.
 */
class BraintreeActivity : BaseActivity<BraintreeContract.Presenter>(), BraintreeContract.View {

    private val REQUEST_BRAINTREE = 8391

    override val presenter: BraintreeContract.Presenter = BraintreePresenter()
    override val layoutResourceId: Int = R.layout.activity_braintree

    private lateinit var token: String
    private lateinit var mBraintreeFragment: BraintreeFragment
    //Nonce is a one-time-use reference to payment info
    private var previousPaymentMethod: PaymentMethodNonce? = null

    override fun getContext(): Context = this@BraintreeActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getToken()
        payBtn.setOnClickListener({ checkPreviousPaymentMethods() })
        repeatPaymentBtn.setOnClickListener({ handlePreviousPaymentMethod() })
    }

    private fun getToken() {
        //It should be replaced with real token from server. This one is only for testing purposes
        token = getString(R.string.mock_token)
    }

    //This functionality will work only if token on the server is created with specific customer_id
    //https://developers.braintreepayments.com/reference/request/client-token/generate/ruby#customer_id
    private fun checkPreviousPaymentMethods() {
        DropInResult.fetchDropInResult(this, token, object : DropInResult.DropInResultListener {
            override fun onError(exception: Exception) {
                Timber.e(">>> error while checking prev payments " + exception.message)
                onBraintreeSubmit()
            }

            override fun onResult(result: DropInResult) {
                if (result.paymentMethodType != null) {
                    // use the icon and name to show in your UI
                    val icon = result.paymentMethodType?.drawable
                    val name = result.paymentMethodType?.localizedName
                    val description = result.paymentMethodNonce?.description

                    lastPaymentInfoContainer.visibility = View.VISIBLE
                    icon?.let {
                        paymentIcon.setImageResource(it)
                    }
                    name?.let {
                        paymentTitle.setText(it)
                    }
                    description?.let {
                        paymentMethodDescription.text = description
                    }

                    if (result.paymentMethodType == PaymentMethodType.ANDROID_PAY) {
                        // The last payment method the user used was Android Pay.
                        // The Android Pay flow will need to be performed by the
                        // user again at the time of checkout.
                    } else {
                        // Use the payment method show in your UI and charge the user
                        // at the time of checkout.
                        val paymentMethod = result.paymentMethodNonce
                        if (paymentMethod != null) {
                            previousPaymentMethod = paymentMethod
                            handlePreviousPaymentMethod()
                        } else {
                            onBraintreeSubmit()
                        }
                    }
                } else {
                    onBraintreeSubmit()
                }
            }
        })
    }

    private fun handlePreviousPaymentMethod() {
        prepareBraintreeFragment()
        displayPaymentInfo(previousPaymentMethod)
        when (previousPaymentMethod) {
            is PayPalAccountNonce -> payWithPayPal()
            is CardNonce -> payWithCreditCard()
        }
    }

    private fun payWithPayPal() {
        startBillingAgreement()
    }

    private fun payWithCreditCard() {
        //TODO: handle credit card payment
        (previousPaymentMethod as CardNonce)

//        Card.tokenize(mBraintreeFragment, )
        Timber.e(">>> payment with credit card is not implemented yet")
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
                    Timber.d(">>> payment result: " + result?.paymentMethodNonce)
                }
                Activity.RESULT_CANCELED -> {
                    // the user canceled
                    Timber.e(">>> payment canceled by user")
                }
                else -> {
                    // handle errors here, an exception may be available in
                    val error = data.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception?
                    Timber.e(">>> payment error " + error?.message)
                }
            }
        }
    }

    private fun prepareBraintreeFragment() {
        try {
            mBraintreeFragment = BraintreeFragment.newInstance(this, token)
            // mBraintreeFragment is ready to use!
            setFragment(mBraintreeFragment, R.id.container)
        } catch (e: InvalidArgumentException) {
            Timber.e(">>> Error while initializing braintree fragment")
            // There was an issue with your authorization string.
        }
    }

    fun startBillingAgreement() {
        val request = PayPalRequest()
                .localeCode("US")
                .billingAgreementDescription("Your agreement description")
        PayPal.requestBillingAgreement(mBraintreeFragment, request)
    }

    fun onPaymentMethodNonceCreated(paymentMethodNonce: PaymentMethodNonce) {
        // Send nonce to server
        val nonce = paymentMethodNonce.nonce
        Timber.d(">>> Payment nonce was created: " + nonce)
        displayPaymentInfo(paymentMethodNonce)
    }

    fun displayPaymentInfo(paymentMethodNonce: PaymentMethodNonce?) {
        var text = ""
        when (paymentMethodNonce) {
            is PayPalAccountNonce -> {
                // Access additional information
                val email = paymentMethodNonce.email
                val firstName = paymentMethodNonce.firstName
                val lastName = paymentMethodNonce.lastName
                val phone = paymentMethodNonce.phone

                val billingAddress = paymentMethodNonce.billingAddress
                val shippingAddress = paymentMethodNonce.shippingAddress

                text = getString(R.string.pay_pal_card_info, email, firstName, lastName, phone, billingAddress, shippingAddress)
            }
            is CardNonce -> {
                val lastTwo = paymentMethodNonce.lastTwo
                text = getString(R.string.card_info, lastTwo)
                Timber.e(">>> payment handling with credit card is not implemented yet")
            }
        }

        paymentInfo.text = text
    }

}