package com.yalantis.flow.braintree.sandbox

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.braintreepayments.api.BraintreeFragment
import com.braintreepayments.api.Card
import com.braintreepayments.api.PayPal
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import com.braintreepayments.api.exceptions.InvalidArgumentException
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener
import com.braintreepayments.api.models.CardNonce
import com.braintreepayments.api.models.PayPalAccountNonce
import com.braintreepayments.api.models.PayPalRequest
import com.braintreepayments.api.models.PaymentMethodNonce
import com.yalantis.R
import com.yalantis.base.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_ui_braintree.*
import timber.log.Timber
import com.braintreepayments.api.models.CardBuilder


/**
 * Created by jtsym on 11/2/2017.
 */

//TODO: create class BraintreeRepeatPaymentActivity

class BraintreeCustomUiActivity : BaseActivity<BraintreeContract.Presenter>(), BraintreeContract.View, PaymentMethodNonceCreatedListener {

    private val REQUEST_BRAINTREE = 8391

    override val presenter: BraintreeContract.Presenter = BraintreePresenter()
    override val layoutResourceId: Int = R.layout.activity_custom_ui_braintree

    private lateinit var token: String
    private lateinit var mBraintreeFragment: BraintreeFragment

    fun newIntent(context: Context): Intent = Intent(context, BraintreeCustomUiActivity::class.java)

    override fun getContext(): Context = this@BraintreeCustomUiActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getToken()
        prepareBraintreeFragment()
    }

    override fun onPaymentMethodNonceCreated(paymentMethodNonce: PaymentMethodNonce) {
        // Send nonce to server (!)
        val nonce = paymentMethodNonce.nonce

        Timber.d(">>> Payment nonce was created: " + nonce)
        displayPaymentInfo(paymentMethodNonce)
    }

    private fun getToken() {
        //It should be replaced with real token from server. This one is only for testing purposes
        token = getString(R.string.mock_token)
    }

    fun payWithPayPal(v: View) {
        Timber.d(">>> payment with paypal")
        PayPal.authorizeAccount(mBraintreeFragment)
//what is the difference?
//        startBillingAgreement()//new PayPal authentication
    }

    fun payOnceWithPayPal(v: View) {
        Timber.d(">>> one-time payment with paypal")
        val request = PayPalRequest("1")
                .currencyCode("USD") //Currency codes: https://developer.paypal.com/docs/integration/direct/rest/currency-codes/#paypal-account-payments
                .intent(PayPalRequest.INTENT_AUTHORIZE)
        PayPal.requestOneTimePayment(mBraintreeFragment, request)
    }

    fun payWithCreditCard(v: View) {
        //TODO: add validation
        Timber.d(">>> payment with credit card")
        val cardBuilder = CardBuilder()
                .cardNumber(cardNumberInput.text?.toString())
                .expirationDate(getString(R.string.card_expiration_date,
                        cardExpirationMonthInput.text?.toString(), cardExpirationYearInput.text?.toString()))
                .validate(true)

        Card.tokenize(mBraintreeFragment, cardBuilder)
    }

    private fun startTransaction(previousPaymentMethod: PaymentMethodNonce) {
        presenter.createTransaction(previousPaymentMethod.nonce)
    }

    private fun onBraintreeSubmit() {
        val dropInRequest = DropInRequest().clientToken(token)
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_BRAINTREE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        Timber.d(">>> onActivityResult: requestCode: $requestCode; resultCode: $resultCode")
        if (requestCode == REQUEST_BRAINTREE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    // use the result to update your UI and send the payment method nonce to your server
                    val result: DropInResult? = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT)
                    Timber.d(">>> payment result: " + result?.paymentMethodNonce?.nonce)
                }
                Activity.RESULT_CANCELED -> {
                    Timber.e(">>> payment canceled by user")
                }
                else -> {
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

    private fun displayPaymentInfo(paymentMethodNonce: PaymentMethodNonce?) {
        when (paymentMethodNonce) {
            is PayPalAccountNonce -> {
                // Access additional information
                val email = paymentMethodNonce.email
                val firstName = paymentMethodNonce.firstName
                val lastName = paymentMethodNonce.lastName
                val phone = paymentMethodNonce.phone

                val billingAddress = paymentMethodNonce.billingAddress
                val shippingAddress = paymentMethodNonce.shippingAddress

                payPalPaymentInfo.text = getString(R.string.pay_pal_card_info, email, firstName, lastName, phone, billingAddress, shippingAddress)
                payPalInfoContainer.visibility = View.VISIBLE
            }
            is CardNonce -> {
                val lastTwo = paymentMethodNonce.lastTwo

                cardPaymentInfo.text = getString(R.string.card_info, lastTwo)
                cardInfoContainer.visibility = View.VISIBLE
            }
        }
    }

}