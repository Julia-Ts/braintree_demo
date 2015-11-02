package com.yalantis.contract;

import com.yalantis.model.GithubRepository;

import java.util.List;

public class MainContract {

    public interface View extends BaseMvpView {

        void showRepositories(List<GithubRepository> repositoryList);

        void showProgress();

        void hideProgress();

        void showErrorMessage();
    }

    public interface Presenter extends BaseMvpPresenter<View> {

        void getRepositories();

    }

}
