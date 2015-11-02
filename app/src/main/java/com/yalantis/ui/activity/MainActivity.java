package com.yalantis.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.yalantis.R;
import com.yalantis.contract.MainContract;
import com.yalantis.model.GithubRepository;
import com.yalantis.presenter.MainPresenter;
import com.yalantis.ui.adapter.RepositoryAdapter;
import com.yalantis.ui.adapter.SimpleDividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainContract.View {

    @Bind(R.id.recycler_view_main)
    RecyclerView mRecyclerView;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new MainPresenter();
        mPresenter.attachView(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RepositoryAdapter adapter = new RepositoryAdapter(new RepositoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(GithubRepository repository) {
                startActivity(DetailedActivity.getCallingIntent(MainActivity.this, repository.getId()));
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
    }

    @Override
    public void showRepositories(List<GithubRepository> repositoryList) {
        RepositoryAdapter adapter = (RepositoryAdapter) mRecyclerView.getAdapter();
        adapter.addRepositories(repositoryList);
    }

    @Override
    public void showProgress() {
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage() {
        Snackbar.make(mRecyclerView, "Error", Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab)
    void onClickFab(View view) {
        mPresenter.getRepositories();
    }

    @Override
    public Context getContext() {
        return MainActivity.this;
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

}
