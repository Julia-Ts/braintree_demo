package com.yalantis.util;

import com.yalantis.Const;

import timber.log.Timber;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public final class Logger {

    static {
        if (Const.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.DebugTree {

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
            super.e(message, args);
        }

        @Override
        public void e(Throwable t, String message, Object... args) {
            super.e(t, message, args);
        }
    }

}
