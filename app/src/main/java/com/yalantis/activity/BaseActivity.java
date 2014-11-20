package com.yalantis.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.yalantis.App;
import com.yalantis.R;
import com.yalantis.event.BaseEvent;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public class BaseActivity extends Activity {

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

}
