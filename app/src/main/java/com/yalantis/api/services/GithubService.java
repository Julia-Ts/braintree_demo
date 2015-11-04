package com.yalantis.api.services;

import com.yalantis.api.ApiSettings;
import com.yalantis.model.Repository;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface GithubService {

    @GET(ApiSettings.ORGANIZATION_REPOS)
    Call<List<Repository>> getOrganizationRepos(
            @Path(ApiSettings.PATH_ORGANIZATION) String organizationName,
            @Query(ApiSettings.PARAM_REPOS_TYPE) String reposType);

}