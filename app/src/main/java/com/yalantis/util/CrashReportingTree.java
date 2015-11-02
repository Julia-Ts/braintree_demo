package com.yalantis.util;

import timber.log.Timber;

/**
 * This class is used for non-{@link com.yalantis.BuildConfig#DEBUG} builds.
 * This is the best place to setup logging logic for your Release builds.
 * <p/>
 * For example, automatically send crash reports or other info
 * to Crashlytics via Crashlytics.logException(e);
 */
public class CrashReportingTree extends Timber.DebugTree {

    @Override
    public void d(String message, Object... args) {

    }

    @Override
    public void d(Throwable t, String message, Object... args) {

    }

    @Override
    public void i(String message, Object... args) {

    }

    @Override
    public void i(Throwable t, String message, Object... args) {

    }

    @Override
    public void w(String message, Object... args) {

    }

    @Override
    public void w(Throwable t, String message, Object... args) {

    }

    @Override
    public void e(String message, Object... args) {

    }

    @Override
    public void e(Throwable t, String message, Object... args) {

    }

}