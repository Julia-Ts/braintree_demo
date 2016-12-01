package com.yalantis.repository;

import android.support.annotation.NonNull;

import com.yalantis.base.BaseMvpPresenter;
import com.yalantis.base.BaseMvpView;
import com.yalantis.model.Repository;

import java.util.List;

class RepositoryContract {

    interface Presenter extends BaseMvpPresenter<View> {

        void initRepositories();

        void fetchRepositories();

        void onRepositoryClicked(Repository repository);

    }

    interface View extends BaseMvpView {

        void showRepositories(List<Repository> repositoryList);

        void showProgress();

        void hideProgress();

        void showInfoMessage(@NonNull String message);

        void showErrorMessage();

    }

}
