package com.yalantis.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import butterknife.Bind
import butterknife.ButterKnife
import com.trello.navi.component.support.NaviAppCompatActivity
import com.yalantis.dialog.ProgressDialogFragment
import com.yalantis.interfaces.BaseActivityCallback

abstract class BaseActivity : NaviAppCompatActivity(), BaseActivityCallback {

    @Bind(android.R.id.content)
    protected var mRootView: View? = null

    private var mProgressDialog: ProgressDialogFragment? = null

    abstract fun getLayoutResourceId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
    }

    override fun setContentView(layoutId: Int) {
        super.setContentView(layoutId)
        ButterKnife.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ButterKnife.unbind(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showError(error: String) {
        showMessage(error)
    }

    override fun showError(@StringRes strResId: Int) {
        showError(getString(strResId))
    }

    override fun showMessage(message: String) {
        mRootView?.let {
            Snackbar.make(mRootView!!, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun showMessage(@StringRes srtResId: Int) {
        showMessage(getString(srtResId))
    }

    override fun showProgress() {
        mProgressDialog?.let {
            mProgressDialog = ProgressDialogFragment.newInstance()
        }
        if (!mProgressDialog?.isAdded!!) {
            mProgressDialog?.show(supportFragmentManager)
        }
    }

    override fun hideProgress() {
        if (mProgressDialog != null && mProgressDialog!!.isAdded) {
            mProgressDialog?.dismiss()
        }
    }

    override fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = this.currentFocus
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    @JvmOverloads protected fun setFragment(fragment: Fragment, containerId: Int, needBackStack: Boolean = false) {
        val oldFragment = supportFragmentManager.findFragmentById(containerId)

        val transaction = supportFragmentManager.beginTransaction()
        if (oldFragment != null) {
            transaction.detach(oldFragment)
        }
        transaction.attach(fragment)
        transaction.replace(containerId, fragment)
        if (needBackStack) {
            transaction.addToBackStack(fragment.tag)
        }
        transaction.commit()
    }

}
