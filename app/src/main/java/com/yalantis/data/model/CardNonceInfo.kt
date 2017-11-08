package com.yalantis.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by jtsym on 11/6/2017.
 */
open class CardNonceInfo(@PrimaryKey open var id: Int? = 1,
                         open var lastTwo: String? = "") : RealmObject()