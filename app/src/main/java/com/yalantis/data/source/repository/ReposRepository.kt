package com.yalantis.data.source.repository

import com.yalantis.data.Repository
import com.yalantis.interfaces.Manager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by irinagalata on 12/1/16.
 */

class ReposRepository : Manager {

    private val localSource: RepositoryLocalDataSource = RepositoryLocalDataSource().apply {
        init()
    }
    private val remoteSource: RepositoryRemoteDataSource = RepositoryRemoteDataSource().apply {
        init()
    }

    fun getRepositories(organization: String, local: Boolean): Observable<List<Repository>> {
        if (!localSource.isEmpty() && local) {
            return localSource.getRepositories(organization).concatWith(getRemoteRepositories(organization)).toObservable()
        }
        return getRemoteRepositories(organization).toObservable()
    }

    private fun getRemoteRepositories(organization: String): Single<List<Repository>> =
            remoteSource.getRepositories(organization)
                .doOnSuccess { repositories -> saveRepositories(repositories) }
                .observeOn(AndroidSchedulers.mainThread())

    private fun saveRepositories(repositories: List<Repository>) {
        localSource.saveRepositories(repositories)
    }

    fun clearRepositories() {
        localSource.clearRepositories()
    }

    override fun clear() {
        clearRepositories()
        localSource.clear()
    }

}
