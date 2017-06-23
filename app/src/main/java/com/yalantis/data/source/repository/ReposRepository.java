package com.yalantis.data.source.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yalantis.data.Repository;
import com.yalantis.interfaces.Manager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by irinagalata on 12/1/16.
 */

public class ReposRepository implements Manager {

    private RepositoryLocalDataSource mLocalSource;
    private RepositoryRemoteDataSource mRemoteSource;

    public ReposRepository(Context context) {
        mLocalSource = new RepositoryLocalDataSource();
        mRemoteSource = new RepositoryRemoteDataSource();

        mLocalSource.init(context);
        mRemoteSource.init(context);
    }

    /**
     * Retrieves the repositories lists from multiple sources and concatenates them to single observable.
     *
     * @param organization Name of the organization to get repositories
     * @param local        if {@code true} returns repositories from both local and remote sources,
     *                     if {@code false} returns from remote only
     * @return {@link Observable} that emits the lists of repositories
     */
    public Observable<List<Repository>> getRepositories(@NonNull String organization, boolean local) {
        if (!mLocalSource.isEmpty() && local) {
            return mLocalSource.getRepositories(organization).concatWith(getRemoteRepositories(organization)).toObservable();
        }

        return getRemoteRepositories(organization).toObservable();
    }

    private Single<List<Repository>> getRemoteRepositories(String organization) {
        return mRemoteSource.getRepositories(organization)
                .doOnSuccess(new Consumer<List<Repository>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Repository> repositories) throws Exception {
                        saveRepositories(repositories);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void saveRepositories(List<Repository> repositories) {
        mLocalSource.saveRepositories(repositories);
    }

    public void clearRepositories() {
        mLocalSource.clearRepositories();
    }

    @Override
    public void clear() {
        clearRepositories();
        mLocalSource.clear();
    }

}
