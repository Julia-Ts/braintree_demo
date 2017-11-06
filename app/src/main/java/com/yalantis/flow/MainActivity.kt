package com.yalantis.flow

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.yalantis.R
import com.yalantis.flow.braintree.sandbox.BraintreeCustomUiActivity
import com.yalantis.flow.braintree.sandbox.BraintreeDropUiActivity

/**
 * Created by jtsym on 11/6/2017.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main_menu)
    }

    fun onBraintreeDropUiBtnClick(v: View) {
        startActivity(BraintreeDropUiActivity().newIntent(this))
    }

    fun onCustomUiBtnClick(v: View) {
        startActivity(BraintreeCustomUiActivity().newIntent(this))
    }

}