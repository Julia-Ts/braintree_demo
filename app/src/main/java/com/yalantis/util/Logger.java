package com.yalantis.util;

import android.text.TextUtils;

import com.yalantis.Const;

import timber.log.Timber;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public final class Logger {
    private static final String TAG = "Changers";

    /**
     * Set true or false if you want read logs or not
     */
    private static boolean logEnabled_d = Const.DEBUG;
    private static boolean logEnabled_i = Const.DEBUG;
    private static boolean logEnabled_e = Const.DEBUG;

    public static void d() {
        if (logEnabled_d) {
            android.util.Log.v(TAG, getLocation());
        }
    }

    public static void d(String msg) {
        if (logEnabled_d) {
            android.util.Log.d(TAG, msg);
        }
    }


    public static void i() {
        if (logEnabled_i) {
            android.util.Log.i(TAG, getLocation());
        }
    }

    public static void e(String msg) {
        if (logEnabled_e) {
            android.util.Log.e(TAG, getLocation() + msg);
        }
    }

    public static void e(String msg, Throwable e) {
        if (logEnabled_e) {
            android.util.Log.e(TAG, getLocation() + msg, e);
        }
    }

    public static void e(Throwable e) {
        if (logEnabled_e) {
            android.util.Log.e(TAG, getLocation(), e);
        }
    }

    public static void e() {
        if (logEnabled_e) {
            android.util.Log.e(TAG, getLocation());
        }
    }

    private static String getLocation() {
        final String className = Logger.class.getName();
        final StackTraceElement[] traces = Thread.currentThread()
                .getStackTrace();
        boolean found = false;

        for (int i = 0; i < traces.length; i++) {
            StackTraceElement trace = traces[i];

            try {
                if (found) {
                    if (!trace.getClassName().startsWith(className)) {
                        Class<?> clazz = Class.forName(trace.getClassName());
                        return "[" + getClassName(clazz) + ":"
                                + trace.getMethodName() + ":"
                                + trace.getLineNumber() + "]: ";
                    }
                } else if (trace.getClassName().startsWith(className)) {
                    found = true;
                    continue;
                }
            } catch (ClassNotFoundException e) {
            }
        }

        return "[]: ";
    }

    private static String getClassName(Class<?> clazz) {
        if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.getSimpleName())) {
                return clazz.getSimpleName();
            }

            return getClassName(clazz.getEnclosingClass());
        }

        return "";
    }

}
