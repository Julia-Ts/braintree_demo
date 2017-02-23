package com.yalantis.data.source.repository;

import android.support.annotation.NonNull;

import com.yalantis.data.Repository;
import com.yalantis.data.RepositoryFields;
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
        return Observable.just((List<Repository>) getRealm()
                .where(Repository.class)
                .findAllSorted(RepositoryFields.STARS_COUNT, Sort.DESCENDING))
                .toSingle();
    }

    @Override
    public void saveRepositories(final List<Repository> repositories) {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(repositories);
            }
        });
    }

    @Override
    public void clearRepositories() {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Repository.class);
            }
        });
    }

    @Override
    public boolean isEmpty() {
        return getRealm().where(Repository.class).count() > 0;
    }

}
