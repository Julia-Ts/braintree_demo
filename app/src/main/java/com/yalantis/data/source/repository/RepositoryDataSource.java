package com.yalantis.data.source.repository;

import android.support.annotation.NonNull;

import com.yalantis.data.Repository;

import java.util.List;

/**
 * Created by irinagalata on 12/1/16.
 */

interface RepositoryDataSource {

    io.reactivex.Single<List<Repository>> getRepositories(@NonNull String organization);

    void saveRepositories(List<Repository> repositories);

    void clearRepositories();

    boolean isEmpty();

}
