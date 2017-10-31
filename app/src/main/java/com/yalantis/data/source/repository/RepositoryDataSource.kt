package com.yalantis.data.source.repository

import com.yalantis.data.Repository
import io.reactivex.Single


/**
 * Created by irinagalata on 12/1/16.
 */

internal interface RepositoryDataSource {

    fun getRepositories(organization: String): Single<List<Repository>>

    fun saveRepositories(repositories: List<Repository>)

    fun clearRepositories()

    fun isEmpty(): Boolean

}
