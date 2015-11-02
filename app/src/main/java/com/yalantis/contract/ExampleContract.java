package com.yalantis.contract;

import android.support.annotation.NonNull;

import com.yalantis.model.GithubRepository;

import java.util.List;

public class ExampleContract {

    public interface Presenter extends BaseMvpPresenter<View> {

        void getRepositories();

        void onRepositoryClicked(GithubRepository repository);

    }

    public interface View extends BaseMvpView {

        void showRepositories(List<GithubRepository> repositoryList);

        void showProgress();

        void hideProgress();

        void showInfoMessage(@NonNull String message);

        void showErrorMessage();

    }

}
