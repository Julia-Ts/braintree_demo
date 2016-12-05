package com.yalantis.api.services;

import com.yalantis.api.ApiSettings;
import com.yalantis.data.Repository;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Single;

public interface GithubService {

    @GET(ApiSettings.ORGANIZATION_REPOS)
    Single<List<Repository>> getOrganizationRepos(
            @Path(ApiSettings.PATH_ORGANIZATION) String organizationName);

}