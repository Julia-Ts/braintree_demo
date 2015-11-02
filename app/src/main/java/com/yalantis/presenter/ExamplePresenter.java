package com.yalantis.presenter;

import com.yalantis.App;
import com.yalantis.api.task.ApiTask;
import com.yalantis.contract.ExampleContract;
import com.yalantis.model.Repository;

import java.util.List;

import retrofit.Call;

/**
 * Created by Oleksii Shliama.
 */
public class ExamplePresenter implements ExampleContract.Presenter {

    private static final String ORGANIZATION_NAME = "Yalantis";
    private static final String REPOS_TYPE = "public";

    private ExampleContract.View mView;

    private Call<List<Repository>> mReposCall;

    @Override
    public void attachView(ExampleContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mReposCall != null) {
            mReposCall.cancel();
        }
    }

    @Override
    public void initRepositories(boolean loadIfEmpty) {
        List<Repository> storedRepositories = App.getDataManager().getRepositories();
        if (loadIfEmpty && storedRepositories.isEmpty()) {
            fetchRepositories();
        } else {
            mView.showRepositories(storedRepositories);
        }
    }

    @Override
    public void fetchRepositories() {
        mView.showProgress();

        mReposCall = App.getApiManager().getOrganizationRepos(ORGANIZATION_NAME, REPOS_TYPE);
        mReposCall.enqueue(new ApiTask<List<Repository>>() {
            @Override
            protected void onSuccess(List<Repository> response) {
                mView.hideProgress();
                onRepositoriesLoaded(response);
            }

            @Override
            protected void onError() {
                mView.hideProgress();
                mView.showErrorMessage();
            }
        });
    }

    private void onRepositoriesLoaded(List<Repository> loadedRepositories) {
        App.getDataManager().storeRepositories(loadedRepositories);
        initRepositories(false);
    }

    @Override
    public void onRepositoryClicked(Repository repository) {
        mView.showInfoMessage("Repository has " + repository.getStarsCount() + " stars.");
    }

}
