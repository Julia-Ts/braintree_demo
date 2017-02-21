package com.yalantis.data.source.repository;

import android.support.annotation.NonNull;

import com.yalantis.data.Repository;
import com.yalantis.data.source.base.BaseLocalDataSource;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import rx.Observable;
import rx.Single;

/**
 * Created by irinagalata on 12/1/16.
 */

class RepositoryLocalDataSource extends BaseLocalDataSource implements RepositoryDataSource {

    @Override
    public Single<List<Repository>> getRepositories(@NonNull String organization) {
        return Observable.just((List<Repository>) mRealm.where(Repository.class)
                .findAllSorted("starsCount", Sort.DESCENDING)).toSingle();
    }

    @Override
    public void saveRepositories(final List<Repository> repositories) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(repositories);
            }
        });
    }

    @Override
    public void clearRepositories() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Repository.class);
            }
        });
    }

    @Override
    public boolean isEmpty() {
        return mRealm.where(Repository.class).count() > 0;
    }

}
