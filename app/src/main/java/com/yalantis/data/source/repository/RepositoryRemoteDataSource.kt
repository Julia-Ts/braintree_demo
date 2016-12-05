package com.yalantis.data.source.repository

import com.yalantis.data.Repository
import com.yalantis.data.source.base.BaseRemoteDataSource

import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by irinagalata on 12/1/16.
 */

internal class RepositoryRemoteDataSource : BaseRemoteDataSource(), RepositoryDataSource {

    override fun getRepositories(organization: String): Single<List<Repository>> {
        return mGithubService.getOrganizationRepos(organization)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun saveRepositories(repositories: List<Repository>) {

    }

    override fun clearRepositories() {

    }

    override fun isEmpty(): Boolean = false
}
