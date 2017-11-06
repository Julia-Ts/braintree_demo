package com.yalantis.data.model

import io.realm.RealmObject

/**
 * Created by jtsym on 11/6/2017.
 */
//PayPalNonce contains more fields with info, these are just for basic representation
open class PayPalNonceInfo(open var email: String? = "",
                           open var billingAddress: String? = "") : RealmObject()