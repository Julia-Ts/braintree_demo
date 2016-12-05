package com.yalantis.data.source.base

import android.content.Context

/**
 * Created by irinagalata on 12/1/16.
 */

internal interface BaseDataSource {

    fun init(context: Context)

    fun clear()

}
