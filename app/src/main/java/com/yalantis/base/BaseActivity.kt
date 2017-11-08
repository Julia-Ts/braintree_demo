package com.yalantis.base

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.yalantis.dialog.ProgressDialogFragment

abstract class BaseActivity<out T : BasePresenter> : AppCompatActivity(), BaseView {

    private var progressDialog: ProgressDialogFragment? = null
    abstract protected val presenter: T
    abstract protected val layoutResourceId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId)
        presenter.attachView(this)
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

    override fun showError(error: String?) {
        showMessage(error)
    }

    override fun showError(@StringRes strResId: Int) {
        showError(getString(strResId))
    }

    override fun showMessage(message: String?) {
        message?.let {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show()
//            Snackbar.make(currentFocus, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun showMessage(@StringRes srtResId: Int) {
        showMessage(getString(srtResId))
    }

    override fun showProgress() {
        progressDialog?.let {
            progressDialog = ProgressDialogFragment.newInstance()
        }
        if (!progressDialog?.isAdded!!) {
            progressDialog?.show(supportFragmentManager)
        }
    }

    override fun hideProgress() {
        if (progressDialog != null && progressDialog!!.isAdded) {
            progressDialog?.dismiss()
        }
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = this.currentFocus
        view?.let {
            inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    @JvmOverloads protected fun setFragment(fragment: Fragment, containerId: Int, needBackStack: Boolean = false) {
        val oldFragment = fragmentManager.findFragmentById(containerId)

        val transaction = fragmentManager.beginTransaction()
        oldFragment?.let {
            transaction.detach(oldFragment)
        }
        transaction.attach(fragment)
        transaction.replace(containerId, fragment)
        if (needBackStack) {
            transaction.addToBackStack(fragment.tag)
        }
        transaction.commit()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}
