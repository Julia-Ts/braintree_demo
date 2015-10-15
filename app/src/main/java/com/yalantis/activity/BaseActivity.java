package com.yalantis.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yalantis.App;
import com.yalantis.R;
import com.yalantis.event.BaseEvent;

/**
 * Base class for all project activities.
 * Handles variety of views, variables and states that are commonly used in every activity.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String PROGRESS_DIALOG_SHOWN = "progressDialogShown";

    protected Handler handler;
    protected Toolbar mToolbar;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        handler = new Handler();
        initProgressDialog();
    }

    @Override
    public void setContentView(int layoutId) {
        super.setContentView(layoutId);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        // Activity can doesn't have toolbar
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        App.eventBus.registerSticky(this);
    }

    public void onEvent(BaseEvent event) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        App.eventBus.unregister(this);
        if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
    }

    public void removeStickyEvent(final Class<?> eventType) {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                App.eventBus.removeStickyEvent(eventType);
            }
        }, 100);
    }

    protected <T extends BaseEvent> void removeStickyEvent(final T event) {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                App.eventBus.removeStickyEvent(event);
            }
        }, 100);
    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.finish();
    }

    protected void replaceFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();
        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && getSupportFragmentManager().findFragmentByTag(backStateName) == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).replace(containerId, fragment,
                    backStateName);
            if (addToBackStack) {
                transaction.addToBackStack(backStateName);
            }
            transaction.commit();
        }
    }

    /**
     * Save if progress dialog was shown, so it will be restored. (e.g. after screen rotation)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PROGRESS_DIALOG_SHOWN, mProgressDialog != null && mProgressDialog.isShowing());
    }

    /**
     * Create an instance of progress dialog that can be used to show any progress.
     */
    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(BaseActivity.this);
        mProgressDialog.setTitle("Please wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
    }

    /**
     * Show progress dialog.
     */
    public void showProgress() {
        mProgressDialog.show();
    }

    /**
     * Hide progress dialog.
     */
    public void hideProgress() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    // TODO: Move all code below - in new App

    /**
     * Shows snackbar with given text. Snackbar is used to show any info to user (like toast, but cooler).
     *
     * @param messageText     text to be shown to user
     * @param buttonText      text for clickable action
     * @param buttonListener  listener of clickable action
     * @param buttonTextColor clickable action text color
     * @param showLength      length of displaying
     */
    public void showSnackbar(String messageText, String buttonText, View.OnClickListener buttonListener, int buttonTextColor, int showLength) {
        App.showSnackbar(findViewById(android.R.id.content), messageText, buttonText, buttonListener, buttonTextColor, showLength);
    }

    public void showSnackbar(String messageText, String buttonText, View.OnClickListener buttonListener) {
        showSnackbar(messageText, buttonText, buttonListener, getResources().getColor(R.color.color_accent), Snackbar.LENGTH_LONG);
    }

    public void showSnackbar(String messageText, int showLength) {
        showSnackbar(messageText, null, null, 0, showLength);
    }

    public void showSnackbar(String text) {
        showSnackbar(text, null, null, 0, Snackbar.LENGTH_LONG);
    }

}
