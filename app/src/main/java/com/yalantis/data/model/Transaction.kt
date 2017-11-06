package com.yalantis.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jtsym on 11/3/2017.
 */
class Transaction {

    @SerializedName("message")
    private val mMessage: String? = null

    fun getMessage(): String? {
        return mMessage
    }

}