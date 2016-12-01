package com.yalantis.data.source;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yalantis.data.Repository;
import com.yalantis.data.source.local.RepositoryLocalDataSource;
import com.yalantis.data.source.remote.RepositoryRemoteDataSource;

import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by irinagalata on 12/1/16.
 */

public class ReposRepository implements RepositoryDataSource {

    private static ReposRepository sInstance = null;

    private RepositoryLocalDataSource mLocalSource;
    private RepositoryRemoteDataSource mRemoteSource;
    private boolean mUpdateNeeded = false;

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

    @Override
    public Observable<List<Repository>> getRepositories(@NonNull String organization) {
        if (!mLocalSource.isEmpty() && !mUpdateNeeded) {
            return mLocalSource.getRepositories(organization);
        }

        return getRemoteRepositories(organization);
    }

    private Observable<List<Repository>> getRemoteRepositories(String organization) {
        return mRemoteSource.getRepositories(organization).flatMap(new Func1<List<Repository>, Observable<List<Repository>>>() {
            @Override
            public Observable<List<Repository>> call(List<Repository> repositories) {
                saveRepositories(repositories);
                return Observable.just(repositories);
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                mUpdateNeeded = false;
            }
        });
    }

    @Override
    public void refreshRepositories() {
        mUpdateNeeded = true;
    }

    @Override
    public void saveRepositories(List<Repository> repositories) {
        mLocalSource.saveRepositories(repositories);
    }

    @Override
    public void clearRepositories() {
        mLocalSource.clearRepositories();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}
