package com.yalantis.data.source.base;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by irinagalata on 12/1/16.
 */

public abstract class BaseLocalDataSource implements BaseDataSource {

    private static final ThreadLocal<Realm> THREAD_LOCAL_REALM = new ThreadLocal<>();

    private static final Set<Realm> REALMS_LIST = new HashSet<>();

    @Override
    public void init(Context context) {

    }

    /**
     * Keep in mind if you create reference to instance of Realm
     * you always need to check isClosed() before usage and you
     * can try to access from incorrect thread.
     * That's why had better to use this method and
     * don't create reference to instance of Realm.
     *
     * @return instance of Realm for current thread
     */
    public Realm getRealm() {
        Realm realm = THREAD_LOCAL_REALM.get();
        if(realm == null || realm.isClosed()) {
            realm = getRealmInstance();
            THREAD_LOCAL_REALM.set(realm);
        }
        REALMS_LIST.add(realm);
        return realm;
    }

    public void closeCurrentThreadRealm() {
        Realm realm = THREAD_LOCAL_REALM.get();
        if(realm != null && !realm.isClosed() && !realm.isInTransaction()) {
            realm.close();
            REALMS_LIST.remove(realm);
        }
        THREAD_LOCAL_REALM.set(null);
    }

    public void closeAllThreadsRealms() {
        for(Realm realm: REALMS_LIST) {
            if(realm != null && !realm.isClosed()) {
                realm.close();
            }
        }
        THREAD_LOCAL_REALM.remove();
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

    }

}
