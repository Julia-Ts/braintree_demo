package com.yalantis.api.services

import com.yalantis.api.ApiSettings
import com.yalantis.data.Repository

import retrofit2.http.GET
import retrofit2.http.Path
import rx.Single

interface GithubService {

    @GET(ApiSettings.ORGANIZATION_REPOS)
    fun getOrganizationRepos(
            @Path(ApiSettings.PATH_ORGANIZATION) organizationName: String): Single<List<Repository>>

//    @GET(ApiSettings.ORGANIZATION_REPOS)
//    fun getRepos(
//            @Path(ApiSettings.PATH_ORGANIZATION) organizationName: String): Single<List<NullTn>>

}