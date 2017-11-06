package com.yalantis.data.model

import io.realm.RealmObject

/**
 * Created by jtsym on 11/6/2017.
 */
open class CardNonceInfo(open var lastTwo: String? = "") : RealmObject()