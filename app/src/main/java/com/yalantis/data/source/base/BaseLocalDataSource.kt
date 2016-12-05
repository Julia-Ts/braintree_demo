package com.yalantis.data.source.base

import android.content.Context

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmMigrationNeededException

/**
 * Created by irinagalata on 12/1/16.
 */

abstract class BaseLocalDataSource : BaseDataSource {

    protected lateinit var mRealm: Realm

    override fun init(context: Context) {
        mRealm = getRealmInstance(context)
    }

    private fun getRealmInstance(context: Context): Realm {
        try {
            return Realm.getDefaultInstance()
        } catch (exception: RealmMigrationNeededException) {
            Realm.deleteRealm(RealmConfiguration.Builder(context).build())
            return Realm.getDefaultInstance()
        }

    }

    override fun clear() {
        if (!mRealm.isClosed) {
            mRealm!!.close()
        }
    }

}
