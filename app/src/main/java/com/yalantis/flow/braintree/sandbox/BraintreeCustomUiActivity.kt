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
import com.braintreepayments.api.interfaces.BraintreeErrorListener
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener
import com.braintreepayments.api.models.*
import com.yalantis.R
import com.yalantis.base.BaseActivity
import com.yalantis.data.model.ClientToken
import kotlinx.android.synthetic.main.activity_custom_ui_braintree.*
import timber.log.Timber
import com.braintreepayments.api.exceptions.ErrorWithResponse


/**
 * Created by jtsym on 11/2/2017.
 */

//TODO: create class BraintreeRepeatPaymentActivity

class BraintreeCustomUiActivity : BaseActivity<BraintreeContract.Presenter>(), BraintreeContract.View,
        PaymentMethodNonceCreatedListener, BraintreeErrorListener {

    private val REQUEST_BRAINTREE = 8391

    override val presenter: BraintreeContract.Presenter = BraintreePresenter()
    override val layoutResourceId: Int = R.layout.activity_custom_ui_braintree

    private var token: String? = null
    private lateinit var mBraintreeFragment: BraintreeFragment

    fun newIntent(context: Context): Intent = Intent(context, BraintreeCustomUiActivity::class.java)

    override fun getContext(): Context = this@BraintreeCustomUiActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.getToken()
    }

    override fun onTokenReceived(token: ClientToken) {
        this.token = token.getClientToken()
        //This mocked token can be used for testing purposes until it is not implemented on backend
        //token = getString(R.string.mock_token)
        prepareBraintreeFragment()
    }

    override fun onPaymentMethodNonceCreated(paymentMethodNonce: PaymentMethodNonce) {
        val nonce = paymentMethodNonce.nonce
        Timber.d(">>> Payment nonce was created: " + nonce)

        // Nonce is sent to server here in order to create transaction
        presenter.createTransaction(nonce)
        displayPaymentInfo(paymentMethodNonce)
    }

    /**
     * ErrorWithResponse is called when there are validations errors with the request.
     * Exception is thrown when an error such as a network issue or server error occurs.
     * https://developers.braintreepayments.com/guides/client-sdk/setup/android/v2#register-listeners
     */
    override fun onError(error: java.lang.Exception?) {
        Timber.e(">>> payment failed, error: " + error?.message)
        if (error is ErrorWithResponse) {
            val cardErrors = error.errorFor("creditCard")
            if (cardErrors != null) {
                // There is an issue with the credit card.
                val cardNumberError = cardErrors.errorFor("number")
                if (cardNumberError != null) {
                    // There is an issue with the card number.
                    cardNumberInput.error = cardNumberError.message
                }
                val expirationYearError = cardErrors.errorFor("expirationYear")
                if (expirationYearError != null) {
                    // There is an issue with the expiration year.
                    cardExpirationYearInput.error = expirationYearError.message
                }
                val expirationMonthError = cardErrors.errorFor("expirationMonth")
                if (expirationMonthError != null) {
                    // There is an issue with the expiration month.
                    cardExpirationMonthInput.error = expirationMonthError.message
                }
                val cvvError = cardErrors.errorFor("cvv")
                if (cvvError != null) {
                    // There is an issue with the expiration cvv.
                    cardCvvInput.error = cvvError.message
                }
                //TODO: There are may be other issues, handle them all
            }
        } else {
            showError(error?.message)
        }
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
                .cvv(cardCvvInput.text?.toString())
                .validate(true)

        Card.tokenize(mBraintreeFragment, cardBuilder)
    }

    private fun startTransaction(previousPaymentMethod: PaymentMethodNonce) {

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