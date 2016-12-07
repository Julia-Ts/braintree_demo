package com.yalantis.flow.repository

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yalantis.R
import com.yalantis.base.BaseActivity
import com.yalantis.data.Repository
import com.yalantis.databinding.ActivityExampleBinding

class RepositoryActivity : BaseActivity<RepositoryPresenter, ActivityExampleBinding>(), RepositoryContract.View {

    override val presenter: RepositoryPresenter = RepositoryPresenter()
    override val layoutResourceId: Int = R.layout.activity_example

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView()
        presenter.initRepositories()
    }

    private fun setupRecyclerView() {
        val adapter = RepositoryAdapter({
            presenter.onRepositoryClicked(it)
        })
        binding.recyclerViewMain.adapter = adapter
        binding.recyclerViewMain.layoutManager = LinearLayoutManager(this)
    }

    override fun showRepositories(repositoryList: List<Repository>) {
        val adapter = binding.recyclerViewMain.adapter as RepositoryAdapter
        adapter.addRepositories(repositoryList)
    }

    override fun showProgress() {
        binding.fab.isEnabled = false
        binding.recyclerViewMain.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.fab.isEnabled = true
        binding.recyclerViewMain.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun getContext(): Context {
        return this@RepositoryActivity
    }
}
