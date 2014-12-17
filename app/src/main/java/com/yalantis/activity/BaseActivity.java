package com.yalantis.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.yalantis.App;
import com.yalantis.R;
import com.yalantis.event.BaseEvent;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        handler = new Handler();
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
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(containerId, fragment,
                    backStateName);
            if (addToBackStack) {
                transaction.addToBackStack(backStateName);
            }
            transaction.commit();
        }
    }
}
