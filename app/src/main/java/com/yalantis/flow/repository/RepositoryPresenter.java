package com.yalantis.flow.repository;

import com.yalantis.App;
import com.yalantis.data.Repository;

import java.util.List;

import rx.functions.Action1;
import rx.internal.util.SubscriptionList;

class RepositoryPresenter implements RepositoryContract.Presenter {

    private static final String ORGANIZATION_NAME = "Yalantis";

    private RepositoryContract.View mView;
    private SubscriptionList mSubscriptions = new SubscriptionList();

    @Override
    public void attachView(RepositoryContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        mSubscriptions.unsubscribe();
    }

    @Override
    public void initRepositories() {
        fetchRepositories(true);
    }

    @Override
    public void fetchRepositories() {
        mView.showProgress();
        fetchRepositories(false);
    }

    private void fetchRepositories(boolean local) {
        mSubscriptions.add(App.getReposRepository().getRepositories(ORGANIZATION_NAME, local)
                .subscribe(new Action1<List<Repository>>() {
                    @Override
                    public void call(List<Repository> repositories) {
                        mView.hideProgress();
                        mView.showRepositories(repositories);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.hideProgress();
                        mView.showErrorMessage();
                    }
                }));
    }

    @Override
    public void onRepositoryClicked(Repository repository) {
        mView.showInfoMessage("Repository has " + repository.getStarsCount() + " stars.");
    }

}
