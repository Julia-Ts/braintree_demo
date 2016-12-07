package com.yalantis.data.source.base

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmMigrationNeededException

/**
 * Created by irinagalata on 12/1/16.
 */

abstract class BaseLocalDataSource : BaseDataSource {

    protected lateinit var mRealm: Realm

    override fun init() {
        mRealm = getRealmInstance()
    }

    private fun getRealmInstance(): Realm {
        try {
            return Realm.getDefaultInstance()
        } catch (exception: RealmMigrationNeededException) {
            Realm.deleteRealm(RealmConfiguration.Builder().build())
            return Realm.getDefaultInstance()
        }
    }

    override fun clear() {
        if (!mRealm.isClosed) {
            mRealm.close()
        }
    }

}
