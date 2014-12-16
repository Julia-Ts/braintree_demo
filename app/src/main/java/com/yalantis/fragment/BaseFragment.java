package com.yalantis.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.yalantis.App;
import com.yalantis.event.BaseEvent;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public class BaseFragment extends Fragment {

    protected Activity activity;

    protected Handler handler;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
    }

    @Override
    public void onStart() {
        super.onStart();
        App.eventBus.registerSticky(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        App.eventBus.unregister(this);
    }

    public void onEvent(BaseEvent event) {
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

}
