package com.yalantis.data.source.repository

import com.yalantis.data.Repository
import com.yalantis.interfaces.Manager
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers

/**
 * Created by irinagalata on 12/1/16.
 */

class ReposRepository : Manager {

    private val mLocalSource: RepositoryLocalDataSource = RepositoryLocalDataSource().apply {
        init()
    }
    private val mRemoteSource: RepositoryRemoteDataSource = RepositoryRemoteDataSource().apply {
        init()
    }

    fun getRepositories(organization: String, local: Boolean): Observable<List<Repository>> {
        if (!mLocalSource.isEmpty() && local) {
            return mLocalSource.getRepositories(organization).concatWith(getRemoteRepositories(organization))
        }
        return getRemoteRepositories(organization).toObservable()
    }

    private fun getRemoteRepositories(organization: String): Single<List<Repository>> {
        return mRemoteSource.getRepositories(organization)
                .doOnSuccess { repositories -> saveRepositories(repositories) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun saveRepositories(repositories: List<Repository>) {
        mLocalSource.saveRepositories(repositories)
    }

    fun clearRepositories() {
        mLocalSource.clearRepositories()
    }

    override fun clear() {
        clearRepositories()
        mLocalSource.clear()
    }

}
