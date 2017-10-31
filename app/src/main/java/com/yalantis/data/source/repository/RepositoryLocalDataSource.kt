package com.yalantis.data.source.repository

import com.yalantis.data.Repository
import com.yalantis.data.RepositoryFields
import com.yalantis.data.source.base.BaseLocalDataSource
import io.reactivex.Single
import io.realm.Sort

/**
 * Created by irinagalata on 12/1/16.
 */

internal class RepositoryLocalDataSource : BaseLocalDataSource(), RepositoryDataSource {

    override fun getRepositories(organization: String): Single<List<Repository>> =
            Single.just(realm.where<Repository>(Repository::class.java)
                .findAllSorted(RepositoryFields.STARS_COUNT, Sort.DESCENDING) as List<Repository>)

    override fun saveRepositories(repositories: List<Repository>) {
        realm.executeTransaction { realm -> realm.copyToRealmOrUpdate(repositories) }
    }

    override fun clearRepositories() {
        realm.executeTransaction { realm -> realm.delete(Repository::class.java) }
    }

    override fun isEmpty(): Boolean = realm.where<Repository>(Repository::class.java).count() > 0

}
