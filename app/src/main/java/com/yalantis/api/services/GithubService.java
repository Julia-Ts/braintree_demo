package com.yalantis.api.services;

import com.yalantis.api.ApiSettings;
import com.yalantis.model.Repository;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface GithubService {

    // Using Retrofit Call
    @GET(ApiSettings.ORGANIZATION_REPOS)
    Call<List<Repository>> getOrganizationRepos(
            @Path(ApiSettings.PATH_ORGANIZATION) String organizationName,
            @Query(ApiSettings.PARAM_REPOS_TYPE) String reposType);

    // Using RxJava Observable
    @GET(ApiSettings.ORGANIZATION_REPOS)
    Observable<List<Repository>> getOrganizationReposRx(
            @Path(ApiSettings.PATH_ORGANIZATION) String organizationName,
            @Query(ApiSettings.PARAM_REPOS_TYPE) String reposType);

}