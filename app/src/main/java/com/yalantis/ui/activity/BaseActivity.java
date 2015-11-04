package com.yalantis.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yalantis.Constant;

import de.greenrobot.event.EventBus;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(Constant.Event event) {

    }

}
