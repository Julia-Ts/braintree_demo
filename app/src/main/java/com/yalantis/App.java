package com.yalantis;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.yalantis.data.source.ReposRepository;
import com.yalantis.manager.SharedPrefManager;
import com.yalantis.model.Migration;
import com.yalantis.util.CrashlyticsReportingTree;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class App extends Application {

    private static Context sContext;

    private static SharedPrefManager sSharedPrefManager;

    private static void setupRealmDefaultInstance() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(sContext)
                .name(Constant.Realm.STORAGE_MAIN)
                .schemaVersion(Migration.CURRENT_VERSION)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public static Context getContext() {
        return sContext;
    }

    public static SharedPrefManager getSharedPrefManager() {
        if (sSharedPrefManager == null) {
            sSharedPrefManager = new SharedPrefManager();
            sSharedPrefManager.init(getContext());
        }
        return sSharedPrefManager;
    }

    public static ReposRepository getReposRepository() {
        return ReposRepository.getInstance(sContext);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.sContext = getApplicationContext();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Fabric.with(App.sContext, new Crashlytics());
            Timber.plant(new CrashlyticsReportingTree());
        }

        setupRealmDefaultInstance();
    }

    public void clear() {
        sSharedPrefManager.clear();
    }

}
