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
import com.braintreepayments.api.dropin.utils.PaymentMethodType
import com.braintreepayments.api.exceptions.InvalidArgumentException
import com.braintreepayments.api.BraintreeFragment
import com.braintreepayments.api.PayPal
import com.braintreepayments.api.interfaces.BraintreeErrorListener
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener
import com.braintreepayments.api.models.*
import com.yalantis.data.model.ClientToken

/**
 * Created by jtsym on 11/2/2017.
 */
class BraintreeDropUiActivity : BaseActivity<BraintreeContract.Presenter>(), BraintreeContract.View,
        PaymentMethodNonceCreatedListener, BraintreeErrorListener {

    private val REQUEST_BRAINTREE = 8391

    override val presenter: BraintreeContract.Presenter = BraintreePresenter()
    override val layoutResourceId: Int = R.layout.activity_drop_ui_braintree

    private var token: String? = null
    private lateinit var braintreeFragment: BraintreeFragment
    //Nonce is a one-time-use reference to payment info
    private var previousPaymentMethod: PaymentMethodNonce? = null

    fun newIntent(context: Context): Intent = Intent(context, BraintreeDropUiActivity::class.java)

    override fun getContext(): Context = this@BraintreeDropUiActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.getToken()
    }

    override fun onTokenReceived(token: ClientToken) {
        this.token = token.getClientToken()
        prepareBraintreeFragment()
        payBtn.setOnClickListener({ checkPreviousPaymentMethods() })
        repeatPaymentBtn.setOnClickListener({ handlePreviousPaymentMethod() })
        //This mocked token can be used for testing purposes until it is not implemented on backend
        //token = getString(R.string.mock_token)
    }

    override fun onPaymentMethodNonceCreated(paymentMethodNonce: PaymentMethodNonce) {
        // Nonce is sent to server here in order to create transaction
        startTransaction(paymentMethodNonce)
        Timber.d(">>> Payment nonce was created: " + paymentMethodNonce.nonce)
        displayPaymentInfo(paymentMethodNonce)
    }

    override fun onError(error: java.lang.Exception?) {
        Timber.e(">>> payment failed, error: " + error?.message)
    }

    /** This functionality will work only if token on the server is created with specific customer_id
     * https://developers.braintreepayments.com/reference/request/client-token/generate/ruby#customer_id
     * https://github.com/braintree/braintree-android-drop-in/issues/2
     **/
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
                            Timber.d(">>> previous payment method was found " + previousPaymentMethod)
                            presenter.saveLastPaymentAccountInfo(paymentMethod)
                            handlePreviousPaymentMethod()
                        } else {
                            Timber.d(">>> previous payment method wasn't found ")
                            onBraintreeSubmit()
                        }
                    }
                } else {
                    Timber.d(">>> previous payment method wasn't found ")
                    onBraintreeSubmit()
                }
            }
        })
    }

    private fun handlePreviousPaymentMethod() {
        displayPaymentInfo(previousPaymentMethod)
        when (previousPaymentMethod) {
            is PayPalAccountNonce -> payWithPayPal()
            is CardNonce -> payWithCreditCard()
        }
    }

    private fun payWithPayPal() {
        Timber.d(">>> payment with paypal")
        startBillingAgreement()//new PayPal authentification
//        startTransaction((previousPaymentMethod as PayPalAccountNonce))//last paypal credentials will be used
    }

    private fun payWithCreditCard() {
        Timber.d(">>> payment with credit card")
        startTransaction((previousPaymentMethod as CardNonce))
    }

    private fun startTransaction(previousPaymentMethod: PaymentMethodNonce) {
        presenter.createTransaction(previousPaymentMethod.nonce)
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
                    Timber.d(">>> payment result: " + result?.paymentMethodNonce?.nonce)
                    // If you try to make another payment with this nonce (result?.paymentMethodNonce), you will get an error
                    // "Unknown or expired payment_method_nonce"
                    // because paymentMethodNonce is just a one-time-use payment reference, you cannot reuse it
                    // You can use this nonce only to display info about payment
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

    /**
     * BraintreeFragment - core Braintree class that handles network requests and managing callbacks.
     * Designed as a fragment in order to be tied to lifecycle events
     * https://developers.braintreepayments.com/guides/client-sdk/migration/android/v2#braintreefragment
     */
    private fun prepareBraintreeFragment() {
        try {
            braintreeFragment = BraintreeFragment.newInstance(this, token)
            // braintreeFragment is ready to use!
        } catch (e: InvalidArgumentException) {
            Timber.e(">>> Error while initializing braintree fragment")
            // There was an issue with your authorization string.
        }
    }

    fun startBillingAgreement() {
        val request = PayPalRequest()
                .localeCode("US")
                .billingAgreementDescription("Your agreement description")
        PayPal.requestBillingAgreement(braintreeFragment, request)
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
            }
        }

        paymentInfo.text = text
        lastPaymentInfoContainer.visibility = View.VISIBLE
    }

}