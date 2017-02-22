package com.yalantis.data.source.base;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by irinagalata on 12/1/16.
 */

public abstract class BaseLocalDataSource implements BaseDataSource {

    private static final ThreadLocal<Realm> THREAD_LOCAL_REALM = new ThreadLocal<>();

    private static final List<Realm> REALMS_LIST = new ArrayList<>();

    @Override
    public void init(Context context) {

    }

    public Realm getRealm() {
        Realm realm = THREAD_LOCAL_REALM.get();
        if(realm == null || realm.isClosed()) {
            realm = getRealmInstance();
            THREAD_LOCAL_REALM.set(realm);
        }
        REALMS_LIST.add(realm);
        return realm;
    }

    public void clearRealm() {
        Realm realm = THREAD_LOCAL_REALM.get();
        if(realm != null && !realm.isClosed()) {
            realm.close();
            REALMS_LIST.remove(realm);
        }
        THREAD_LOCAL_REALM.set(null);
    }

    private Realm getRealmInstance() {
        try {
            return Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException exception) {
            Realm.deleteRealm(new RealmConfiguration.Builder().build());
            return Realm.getDefaultInstance();
        }
    }

    @Override
    public void clear() {
        for(Realm realm: REALMS_LIST) {
            if(realm != null && !realm.isClosed()) {
                realm.close();
            }
        }
        THREAD_LOCAL_REALM.remove();
    }

}
