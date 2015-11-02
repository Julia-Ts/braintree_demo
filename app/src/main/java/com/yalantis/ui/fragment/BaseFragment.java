package com.yalantis.ui.fragment;

import android.support.v4.app.Fragment;

import com.yalantis.Constant;

import de.greenrobot.event.EventBus;

public class BaseFragment extends Fragment {

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(Constant.Event event) {

    }

}
