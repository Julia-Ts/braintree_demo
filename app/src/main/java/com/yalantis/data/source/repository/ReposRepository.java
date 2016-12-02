package com.yalantis.data.source.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yalantis.data.Repository;

import java.util.List;

import rx.Observable;
import rx.Single;
import rx.functions.Action1;

/**
 * Created by irinagalata on 12/1/16.
 */

public class ReposRepository {

    private static ReposRepository sInstance = null;

    private RepositoryLocalDataSource mLocalSource;
    private RepositoryRemoteDataSource mRemoteSource;

    private ReposRepository(Context context) {
        mLocalSource = new RepositoryLocalDataSource();
        mRemoteSource = new RepositoryRemoteDataSource();

        mLocalSource.init(context);
        mRemoteSource.init(context);
    }

    public static ReposRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ReposRepository(context);
        }

        return sInstance;
    }

    public Observable<List<Repository>> getRepositories(@NonNull String organization, boolean local) {
        if (!mLocalSource.isEmpty() && local) {
            return mLocalSource.getRepositories(organization).concatWith(getRemoteRepositories(organization));
        }

        return getRemoteRepositories(organization).toObservable();
    }

    private Single<List<Repository>> getRemoteRepositories(String organization) {
        return mRemoteSource.getRepositories(organization)
                .doOnSuccess(new Action1<List<Repository>>() {
                    @Override
                    public void call(List<Repository> repositories) {
                        saveRepositories(repositories);
                    }
                });
    }

    private void saveRepositories(List<Repository> repositories) {
        mLocalSource.saveRepositories(repositories);
    }

    public void clearRepositories() {
        mLocalSource.clearRepositories();
    }

}
