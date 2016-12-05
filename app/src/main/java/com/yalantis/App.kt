package com.yalantis

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.yalantis.data.Migration
import com.yalantis.util.CrashlyticsReportingTree
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Fabric.with(this, Crashlytics())
            Timber.plant(CrashlyticsReportingTree())
        }

        setupRealmDefaultInstance(this)
    }

    private fun setupRealmDefaultInstance(context: Context) {
        val realmConfig = RealmConfiguration.Builder(context)
                .schemaVersion(Migration.CURRENT_VERSION)
                .migration(Migration())
                .build()
        Realm.setDefaultConfiguration(realmConfig)
    }
}
