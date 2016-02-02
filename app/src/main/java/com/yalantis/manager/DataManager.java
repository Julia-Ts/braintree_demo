package com.yalantis.manager;

import android.content.Context;

import com.yalantis.Constant;
import com.yalantis.interfaces.Manager;
import com.yalantis.model.Repository;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmMigrationNeededException;

public class DataManager implements Manager {

    private Realm mRealm;

    @Override
    public void init(Context context) {
        mRealm = getRealmInstance(context);
    }

    private Realm getRealmInstance(Context context) {
        try {
            return Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException exception) {
            Realm.deleteRealm(new RealmConfiguration.Builder(context)
                    .name(Constant.Realm.STORAGE_MAIN).build());
            return Realm.getDefaultInstance();
        }
    }

    public List<Repository> getRepositories() {
        return mRealm.where(Repository.class).findAllSorted("starsCount", Sort.DESCENDING);
    }

    public void storeRepositories(final List<Repository> repositories) {
        try {
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(repositories);
            mRealm.commitTransaction();
        } catch (RealmException e) {
            mRealm.cancelTransaction();
        }
    }

    public void removeRepositories() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.clear(Repository.class);
            }
        });
    }

    @Override
    public void clear() {
        if (mRealm != null && !mRealm.isClosed()) {
            mRealm.close();
        }
    }

}
