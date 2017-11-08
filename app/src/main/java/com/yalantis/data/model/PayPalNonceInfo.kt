package com.yalantis.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by jtsym on 11/6/2017.
 */
//PayPalNonce contains more fields with info, these are just for basic representation
open class PayPalNonceInfo(@PrimaryKey open var id: Int? = 1,
                           open var email: String? = "",
                           open var billingAddress: String? = "") : RealmObject()