package com.yalantis.util;

import android.app.Activity;
import android.widget.Toast;

import com.yalantis.App;

public class Toaster {

    public static void showShort(String value) {
        Toast.makeText(App.getContext(), value, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(int resId) {
        Toast.makeText(App.getContext(), App.getContext().getString(resId),
                Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Activity activity, String value) {
        if (activity != null)
            Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Activity activity, int resId) {
        if (activity != null)
            Toast.makeText(activity, activity.getString(resId),
                    Toast.LENGTH_SHORT).show();
    }
}
