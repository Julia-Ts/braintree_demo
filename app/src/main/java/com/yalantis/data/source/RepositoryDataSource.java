package com.yalantis.data.source;

import android.support.annotation.NonNull;

import com.yalantis.model.Repository;

import java.util.List;

import rx.Observable;

/**
 * Created by irinagalata on 12/1/16.
 */

public interface RepositoryDataSource {

    Observable<List<Repository>> getRepositories(@NonNull String organization);

    void refreshRepositories();

    void saveRepositories(List<Repository> repositories);

    void clearRepositories();

    boolean isEmpty();

}
