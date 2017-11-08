package com.yalantis.flow.braintree.sandbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.braintreepayments.api.BraintreeFragment
import com.braintreepayments.api.Card
import com.braintreepayments.api.PayPal
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

class BraintreeCustomUiActivity : BaseActivity<BraintreeContract.Presenter>(), BraintreeContract.View,
        PaymentMethodNonceCreatedListener, BraintreeErrorListener {

    override val presenter: BraintreeContract.Presenter = BraintreePresenter()
    override val layoutResourceId: Int = R.layout.activity_custom_ui_braintree

    private lateinit var braintreeFragment: BraintreeFragment

    fun newIntent(context: Context): Intent = Intent(context, BraintreeCustomUiActivity::class.java)

    override fun getContext(): Context = this@BraintreeCustomUiActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.getToken()
    }

    override fun onTokenReceived(token: ClientToken) {
        //This mocked token can be used for testing purposes until it is not implemented on backend
        //getString(R.string.mock_token)
        prepareBraintreeFragment(token.getClientToken())
    }

    override fun onPaymentMethodNonceCreated(paymentMethodNonce: PaymentMethodNonce) {
        val nonce = paymentMethodNonce.nonce
        Timber.d(">>> Payment nonce was created: " + nonce)

        // Nonce is sent to server here in order to create transaction
        presenter.createTransaction(nonce)
        presenter.saveLastPaymentAccountInfo(paymentMethodNonce)
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
        startBillingAgreement()
//        reAuthPayPalAndPay() // Use if you want to make user login into PayPal again
    }

    fun payOnceWithPayPal(v: View) {
        Timber.d(">>> one-time payment with paypal")
        val request = PayPalRequest("1")
                .currencyCode("USD") //Currency codes: https://developer.paypal.com/docs/integration/direct/rest/currency-codes/#paypal-account-payments
                .intent(PayPalRequest.INTENT_AUTHORIZE)
        PayPal.requestOneTimePayment(braintreeFragment, request)
    }

    fun payWithCreditCard(v: View) {
        Timber.d(">>> payment with credit card")
        val cardBuilder = CardBuilder()
                .cardNumber(cardNumberInput.text?.toString())
                .expirationDate(getString(R.string.card_expiration_date,
                        cardExpirationMonthInput.text?.toString(), cardExpirationYearInput.text?.toString()))
                .cvv(cardCvvInput.text?.toString())
                .validate(true)//This is Braintree client-side validation

        Card.tokenize(braintreeFragment, cardBuilder)
    }

    private fun prepareBraintreeFragment(token: String?) {
        try {
            braintreeFragment = BraintreeFragment.newInstance(this, token)
            // braintreeFragment is ready to use
        } catch (e: InvalidArgumentException) {
            Timber.e(">>> Error while initializing braintree fragment")
            // There was an issue with your authorization string.
        }
    }

    /**
     * Requests new PayPal authentication
     */
    private fun reAuthPayPalAndPay() {
        PayPal.authorizeAccount(braintreeFragment)
    }

    /**
     * Billing Agreement should be used if you want to perform recurring payments
     * https://developers.braintreepayments.com/guides/paypal/vault/android/v2
     * about billing agreements: https://developer.paypal.com/docs/integration/direct/billing-plans-and-agreements/
     */
    private fun startBillingAgreement() {
        val request = PayPalRequest()
                .localeCode("US")
                .billingAgreementDescription("Your agreement description")

        PayPal.requestBillingAgreement(braintreeFragment, request)
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