package com.yalantis.api.services;

import com.yalantis.api.ApiSettings;
import com.yalantis.model.Repository;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GithubService {

    @GET(ApiSettings.ORGANIZATION_REPOS)
    Observable<List<Repository>> getOrganizationRepos(
            @Path(ApiSettings.PATH_ORGANIZATION) String organizationName);

}