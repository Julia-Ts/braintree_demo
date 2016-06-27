package com.yalantis;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.yalantis.manager.ApiManager;
import com.yalantis.manager.DataManager;
import com.yalantis.manager.SharedPrefManager;
import com.yalantis.util.CrashlyticsReportingTree;

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

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Fabric.with(this, new Crashlytics());
            Timber.plant(new CrashlyticsReportingTree());
        }
    }

    public static Context getContext() {
        return sContext;
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

    public static void logOut() {
        //TODO: log out stuff
        Timber.e("logOut");
    }

}
