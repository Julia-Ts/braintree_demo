package com.yalantis.data.source.repository

import com.yalantis.data.Repository
import com.yalantis.data.source.base.BaseLocalDataSource
import io.realm.Sort
import rx.Observable
import rx.Single

/**
 * Created by irinagalata on 12/1/16.
 */

internal class RepositoryLocalDataSource : BaseLocalDataSource(), RepositoryDataSource {

    override fun getRepositories(organization: String): Single<List<Repository>> {
        return Observable.just(mRealm.where<Repository>(Repository::class.java)
                .findAllSorted("starsCount", Sort.DESCENDING) as List<Repository>).toSingle()
    }

    override fun saveRepositories(repositories: List<Repository>) {
        mRealm.executeTransaction { realm -> realm.copyToRealmOrUpdate(repositories) }
    }

    override fun clearRepositories() {
        mRealm.executeTransaction { realm -> realm.delete(Repository::class.java) }
    }

    override fun isEmpty(): Boolean = mRealm.where<Repository>(Repository::class.java).count() > 0

}
