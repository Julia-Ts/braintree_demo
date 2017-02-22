package com.yalantis.flow.repository;

import com.yalantis.base.BaseMvpPresenterImpl;
import com.yalantis.data.Repository;
import com.yalantis.data.source.repository.ReposRepository;

import java.util.List;

import rx.functions.Action1;

class RepositoryPresenter extends BaseMvpPresenterImpl<RepositoryContract.View> implements RepositoryContract.Presenter {

    private static final String ORGANIZATION_NAME = "Yalantis";

    private ReposRepository mRepository;

    @Override
    public void attachView(RepositoryContract.View view) {
        super.attachView(view);
        mRepository = new ReposRepository(view.getContext());
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
        addSubscription(mRepository.getRepositories(ORGANIZATION_NAME, local)
                .subscribe(new Action1<List<Repository>>() {
                    @Override
                    public void call(List<Repository> repositories) {
                        mView.hideProgress();
                        mView.showRepositories(repositories);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mView.hideProgress();
                        mView.showErrorMessage();
                    }
                }));
    }

    @Override
    public void onRepositoryClicked(Repository repository) {
        mView.showInfoMessage("Repository has " + repository.getStarsCount() + " stars.");
    }

    @Override
    public void detachView() {
        super.detachView();
        mRepository.clear();
    }
}
