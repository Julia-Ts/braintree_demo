package com.yalantis.flow.repository

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yalantis.R
import com.yalantis.base.BaseActivity
import com.yalantis.data.Repository
import kotlinx.android.synthetic.main.activity_example.*

class RepositoryActivity : BaseActivity<RepositoryContract.Presenter>(), RepositoryContract.View {

    override val presenter = RepositoryPresenter()
    override val layoutResourceId: Int = R.layout.activity_example

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView()
        presenter.initRepositories()
        fab.setOnClickListener{presenter.fetchRepositories()}
    }

    private fun setupRecyclerView() {
        val adapter = RepositoryAdapter({
            presenter.onRepositoryClicked(it)
        })
        recyclerViewMain.adapter = adapter
        recyclerViewMain.layoutManager = LinearLayoutManager(this)
    }

    override fun showRepositories(repositoryList: List<Repository>) {
        val adapter = recyclerViewMain.adapter as RepositoryAdapter
        adapter.addRepositories(repositoryList)
    }

    override fun showProgress() {
        fab.isEnabled = false
        recyclerViewMain.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        fab.isEnabled = true
        recyclerViewMain.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun getContext(): Context {
        return this@RepositoryActivity
    }
}
