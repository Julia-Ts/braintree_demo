package com.yalantis.flow.braintree.sandbox

import android.content.Context
import com.yalantis.R
import com.yalantis.base.BaseActivity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import kotlinx.android.synthetic.main.activity_drop_ui_braintree.*
import timber.log.Timber
import com.braintreepayments.api.interfaces.BraintreeErrorListener
import com.braintreepayments.api.models.*
import com.yalantis.data.model.ClientToken

/**
 * Created by jtsym on 11/2/2017.
 */
class BraintreeDropUiActivity : BaseActivity<BraintreeContract.Presenter>(), BraintreeContract.View, BraintreeErrorListener {

    private val REQUEST_BRAINTREE = 8391

    override val presenter: BraintreeContract.Presenter = BraintreePresenter()
    override val layoutResourceId: Int = R.layout.activity_drop_ui_braintree

    private var token: String? = null

    fun newIntent(context: Context): Intent = Intent(context, BraintreeDropUiActivity::class.java)

    override fun getContext(): Context = this@BraintreeDropUiActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.getToken()
        payBtn.setOnClickListener({ onBraintreeSubmit() })
        repeatPaymentBtn.setOnClickListener({ startActivity(BraintreeRepeatPaymentActivity().newIntent(this)) })
    }

    override fun onTokenReceived(token: ClientToken) {
        this.token = token.getClientToken()
        payBtn.isEnabled = true
        //This mocked token can be used for testing purposes until it is not implemented on backend
        //token = getString(R.string.mock_token)
    }

    override fun onError(error: java.lang.Exception?) {
        Timber.e(">>> payment failed, error: " + error?.message)
        showError("payment failed, error: " + error?.message)
    }

    private fun startTransaction(previousPaymentMethod: PaymentMethodNonce) {
        presenter.createTransaction(previousPaymentMethod.nonce)
    }

    private fun onBraintreeSubmit() {
        val dropInRequest = DropInRequest().clientToken(token)
        //DataCollector enables you to collect data about a customer's device and correlate it with a session identifier on your server.
        //https://developers.braintreepayments.com/guides/paypal/vault/android/v2#collecting-device-data
        dropInRequest.collectDeviceData(true)
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_BRAINTREE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_BRAINTREE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    // use the result to update your UI and send the payment method nonce to your server
                    val result: DropInResult? = data?.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT)
                    val paymentMethodNonce = result?.paymentMethodNonce
                    Timber.d(">>> payment result: " + paymentMethodNonce?.nonce)
                    displayPaymentInfo(paymentMethodNonce)
                    sendDeviceData(result?.deviceData)
                    paymentMethodNonce?.let {
                        startTransaction(paymentMethodNonce)
                        presenter.saveLastPaymentAccountInfo(paymentMethodNonce)
                        Timber.d(">>> Payment nonce was created: " + paymentMethodNonce.nonce)
                        showMessage("Payment was sent to server successfully")
                    }
                }
                Activity.RESULT_CANCELED -> {
                    Timber.e(">>> payment canceled by user")
                    showMessage("payment was canceled by user")
                }
                else -> {
                    val error = data?.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception?
                    Timber.e(">>> payment error " + error?.message)
                    showError("payment error " + error?.message)
                }
            }
        }
    }

    /**
     * Send the device data string response from Drop-in to your server to be included in verification or transaction requests
     * Collecting device data from your customers is required when initiating non-recurring transactions from Vault records.
     * https://developers.braintreepayments.com/guides/paypal/vault/android/v2#collecting-device-data
     */
    private fun sendDeviceData(deviceData: String?) {
        //TODO: implement sending device data according to your server logic
    }

    private fun displayPaymentInfo(paymentMethodNonce: PaymentMethodNonce?) {
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
            }
        }

        paymentInfo.text = text
        lastPaymentInfoContainer.visibility = View.VISIBLE
        repeatPaymentBtn.isEnabled = true
    }

}