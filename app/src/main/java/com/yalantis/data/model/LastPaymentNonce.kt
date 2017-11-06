package com.yalantis.data.model

import com.braintreepayments.api.models.PaymentMethodNonce
import io.realm.RealmObject

/**
 * Created by jtsym on 11/6/2017.
 */
open class LastPaymentNonce(open var nonce: PaymentMethodNonce? = null) : RealmObject()