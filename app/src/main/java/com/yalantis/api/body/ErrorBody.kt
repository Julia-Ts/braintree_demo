package com.yalantis.api.body

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

import java.io.IOException

import retrofit2.Response

/**
 * Created by voltazor on 16/06/16.
 */
class ErrorBody {

    @SerializedName("code")
    var code: Int = 0

    @SerializedName("error")
    var message: String? = null

    override fun toString(): String {
        return "{code:$code, message:\"$message\"}"
    }

    companion object {

        //TODO: apply real error codes
        val INVALID_CREDENTIALS = 101
        val EMAIL_IS_TAKEN = 102
        val INVALID_TOKEN = 103

        private val GSON = Gson()

        fun parseError(response: Response<*>?): ErrorBody? {
            var error: ErrorBody? = null
                try {
                    error = GSON.fromJson(response?.errorBody()?.string(), ErrorBody::class.java)
                } catch (ignored: IOException) {
                    return null
                }
            return error
        }
    }

}
