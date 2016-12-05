package com.yalantis;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.yalantis.data.Migration;
import com.yalantis.data.source.repository.ReposRepository;
import com.yalantis.manager.SharedPrefManager;
import com.yalantis.util.CrashlyticsReportingTree;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class App extends Application {

    private static void setupRealmDefaultInstance(Context context) {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                .schemaVersion(Migration.CURRENT_VERSION)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public static void logOut(Context context) {
        //TODO: log out stuff
        SharedPrefManager.getInstance(context).clear();
        new ReposRepository(context).clear();
        Timber.e("logOut");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Fabric.with(this, new Crashlytics());
            Timber.plant(new CrashlyticsReportingTree());
        }

        setupRealmDefaultInstance(this);
    }

}
