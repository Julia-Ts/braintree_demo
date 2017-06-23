package com.yalantis.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.trello.navi2.component.support.NaviAppCompatActivity;
import com.yalantis.dialog.ProgressDialogFragment;
import com.yalantis.interfaces.BaseActivityCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends NaviAppCompatActivity implements BaseActivityCallback {

    @Nullable
    @BindView(android.R.id.content)
    protected View mRootView;

    private ProgressDialogFragment mProgressDialog;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
    }

    @Override
    public final void setContentView(int layoutId) {
        super.setContentView(layoutId);
        mBind = ButterKnife.bind(this);
    }

    protected abstract int getLayoutResourceId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showError(String message) {
        showMessage(message);
    }

    @Override
    public void showError(@StringRes int strResId) {
        showError(getString(strResId));
    }

    @Override
    public void showMessage(String message) {
        if (mRootView != null) {
            Snackbar.make(mRootView, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(@StringRes int srtResId) {
        showMessage(getString(srtResId));
    }

    @Override
    public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialogFragment.newInstance();
        }
        if (!mProgressDialog.isAdded()) {
            mProgressDialog.show(getSupportFragmentManager());
        }
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isAdded()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) {
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    protected void setFragment(Fragment fragment, int containerId) {
        setFragment(fragment, containerId, false);
    }

    protected void setFragment(Fragment fragment, int containerId, boolean needBackStack) {
        Fragment oldFragment = getSupportFragmentManager().findFragmentById(containerId);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (oldFragment != null) {
            transaction.detach(oldFragment);
        }
        transaction.attach(fragment);
        transaction.replace(containerId, fragment);
        if (needBackStack) {
            transaction.addToBackStack(fragment.getTag());
        }
        transaction.commit();
    }

}
