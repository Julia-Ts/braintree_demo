package com.yalantis.contract;

import android.support.annotation.NonNull;

import com.yalantis.model.Repository;

import java.util.List;

public class ExampleContract {

    public interface Presenter extends BaseMvpPresenter<View> {

        void initRepositories(boolean loadIfEmpty);

        void fetchRepositories();

        void onRepositoryClicked(Repository repository);

    }

    public interface View extends BaseMvpView {

        void showRepositories(List<Repository> repositoryList);

        void showProgress();

        void hideProgress();

        void showInfoMessage(@NonNull String message);

        void showErrorMessage();

    }

}
