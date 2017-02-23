package com.yalantis.data.source.repository;

import android.support.annotation.NonNull;

import com.yalantis.data.Repository;
import com.yalantis.data.source.base.BaseRemoteDataSource;

import java.util.List;

import rx.Single;

/**
 * Created by irinagalata on 12/1/16.
 */

class RepositoryRemoteDataSource extends BaseRemoteDataSource implements RepositoryDataSource {

    @Override
    public Single<List<Repository>> getRepositories(@NonNull String organization) {
        return mGithubService.getOrganizationRepos(organization);
    }

    @Override
    public void saveRepositories(List<Repository> repositories) {

    }

    @Override
    public void clearRepositories() {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}
