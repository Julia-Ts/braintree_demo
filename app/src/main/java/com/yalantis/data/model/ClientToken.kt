package com.yalantis.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jtsym on 11/3/2017.
 */
class ClientToken {

    @SerializedName("client_token")
    private val mClientToken: String? = null

    fun getClientToken(): String? {
        return mClientToken
    }

}