package com.yalantis.api.services

import com.yalantis.api.ApiSettings
import com.yalantis.data.Repository
import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {

    @GET(ApiSettings.ORGANIZATION_REPOS)
    fun getOrganizationRepos(
            @Path(ApiSettings.PATH_ORGANIZATION) organizationName: String): Single<List<Repository>>

}