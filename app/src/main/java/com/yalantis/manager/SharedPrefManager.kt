package com.yalantis.manager

import android.content.Context
import com.yalantis.util.CachedValue

/**
 * Created by warko on 9/6/17.
 */
object SharedPrefManager {

    private val USER_TOKEN = "token"
    private val USER_EXCHANGE_TOKEN = "exchange_token"
    private val USER_ID = "user_id"
    const val NAME = "sharedPrefs"

    fun init(context: Context?) {
        context?.let {
            CachedValue.initialize(context.getSharedPreferences(NAME, Context.MODE_PRIVATE))
        }
    }

    var userAuthToken: String? by CachedValue(USER_TOKEN, "", String::class.java)
    var userExchangeToken: String? by CachedValue(USER_EXCHANGE_TOKEN, "", String::class.java)
    var userId: Int? by CachedValue(USER_ID, 0, Int::class.java)

    fun clear() {
        CachedValue.clear()
    }

}