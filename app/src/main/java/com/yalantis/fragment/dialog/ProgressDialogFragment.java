package com.yalantis.fragment.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.yalantis.R;

/**
 * Created by Aleksandr on 01.12.2014.
 */
public class ProgressDialogFragment extends DialogFragment {

    private DialogInterface.OnCancelListener cancelListener;

    public static ProgressDialogFragment newInstance() {
        return new ProgressDialogFragment();
    }

    public String getFragmentTag() {
        return ProgressDialogFragment.class.getSimpleName();
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        this.cancelListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.dlg_wait));
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (cancelListener != null) {
            cancelListener.onCancel(dialog);
        }
    }
}
