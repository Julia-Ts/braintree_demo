package com.yalantis.data.source.base;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by irinagalata on 12/1/16.
 */

public abstract class BaseLocalDataSource implements BaseDataSource {

    protected Realm mRealm;

    @Override
    public void init(Context context) {
        mRealm = getRealmInstance(context);
    }

    private Realm getRealmInstance(Context context) {
        try {
            return Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException exception) {
            Realm.deleteRealm(new RealmConfiguration.Builder(context).build());
            return Realm.getDefaultInstance();
        }
    }

    @Override
    public void clear() {
        if (mRealm != null && !mRealm.isClosed()) {
            mRealm.close();
        }
    }

}
