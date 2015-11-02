package com.yalantis;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.yalantis.manager.ApiManager;
import com.yalantis.manager.DataManager;
import com.yalantis.manager.SharedPrefManager;
import com.yalantis.util.CrashReportingTree;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class App extends Application {

    private static Context sContext;

    private static ApiManager sApiManager;
    private static DataManager sDataManager;
    private static SharedPrefManager sSharedPrefManager;

    @Override
    public void onCreate() {
        super.onCreate();
        App.sContext = getApplicationContext();

        Fabric.with(App.sContext, new Crashlytics.Builder().core(
                new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());
        Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new CrashReportingTree());
    }

    public static Context getContext() {
        return sContext;
    }

    public void clear() {
        sApiManager.clear();
        sDataManager.clear();
        sSharedPrefManager.clear();
    }

    public static ApiManager getApiManager() {
        if (sApiManager == null) {
            sApiManager = new ApiManager();
            sApiManager.init(getContext());
        }
        return sApiManager;
    }

    public static DataManager getDataManager() {
        if (sDataManager == null) {
            sDataManager = new DataManager();
            sDataManager.init(getContext());
        }
        return sDataManager;
    }

    public static SharedPrefManager getSharedPrefManager() {
        if (sSharedPrefManager == null) {
            sSharedPrefManager = new SharedPrefManager();
            sSharedPrefManager.init(getContext());
        }
        return sSharedPrefManager;
    }

}
