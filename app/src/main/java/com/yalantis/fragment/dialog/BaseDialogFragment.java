package com.yalantis.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.yalantis.R;
import com.yalantis.event.BaseEvent;
import com.yalantis.interfaces.AlertDialogListener;

import de.greenrobot.event.EventBus;

public abstract class BaseDialogFragment extends DialogFragment {

    protected Activity activity;
    protected AlertDialogListener alertListener;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(BaseEvent event) {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public abstract String getFragmentTag();

    public void show(FragmentManager manager) {
        super.show(manager, getFragmentTag());
    }

    public void setAlertListener(AlertDialogListener alertListener) {
        this.alertListener = alertListener;
    }
}
