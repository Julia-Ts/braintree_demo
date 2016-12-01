package com.yalantis.data.source.local;

import android.support.annotation.NonNull;

import com.yalantis.data.source.RepositoryDataSource;
import com.yalantis.data.source.base.BaseLocalDataSource;
import com.yalantis.model.Repository;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import rx.Observable;

/**
 * Created by irinagalata on 12/1/16.
 */

public class RepositoryLocalDataSource extends BaseLocalDataSource implements RepositoryDataSource {

    @Override
    public Observable<List<Repository>> getRepositories(@NonNull String organization) {
        return Observable.just((List<Repository>) mRealm.where(Repository.class).findAllSorted("starsCount", Sort.DESCENDING));
    }

    @Override
    public void refreshRepositories() {

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
                realm.clear(Repository.class);
            }
        });
    }

    @Override
    public boolean isEmpty() {
        return mRealm.where(Repository.class).findAll().isEmpty();
    }

}
