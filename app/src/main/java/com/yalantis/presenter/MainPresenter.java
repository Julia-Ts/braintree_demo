package com.yalantis.presenter;

import com.yalantis.App;
import com.yalantis.contract.MainContract;
import com.yalantis.model.GithubRepository;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Oleksii Shliama.
 */
public class MainPresenter implements MainContract.Presenter {

    private static final String ORGANIZATION_NAME = "Yalantis";
    private static final String REPOS_TYPE = "public";

    private MainContract.View mView;

    private Call<List<GithubRepository>> mReposCall;

    @Override
    public void attachView(MainContract.View view) {
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
    public void getRepositories() {
        mView.showProgress();

        mReposCall = App.getApiManager().getOrganizationRepos(ORGANIZATION_NAME, REPOS_TYPE);
        mReposCall.enqueue(new Callback<List<GithubRepository>>() {
            @Override
            public void onResponse(Response<List<GithubRepository>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    mView.hideProgress();
                    mView.showRepositories(response.body());
                } else {
                    mView.hideProgress();
                    mView.showErrorMessage();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mView.hideProgress();
                mView.showErrorMessage();
            }
        });
    }

}
