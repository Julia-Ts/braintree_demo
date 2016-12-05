package com.yalantis.flow.repository

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import butterknife.Bind
import butterknife.ButterKnife
import butterknife.OnClick
import com.yalantis.R
import com.yalantis.base.BaseMvpActivity
import com.yalantis.data.Repository

class RepositoryActivity : BaseMvpActivity<RepositoryContract.View, RepositoryPresenter>(), RepositoryContract.View {

    override fun presenterInstance(): RepositoryPresenter = RepositoryPresenter()

    @Bind(R.id.recycler_view_main)
    internal var mRecyclerView: RecyclerView? = null
    @Bind(R.id.progress_bar)
    internal var mProgressBar: ProgressBar? = null
    @Bind(R.id.fab)
    internal var mFloatingActionButton: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
        ButterKnife.bind(this)
        setupRecyclerView()
        mPresenter.initRepositories()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_example
    }

    private fun setupRecyclerView() {
        //FIXME
        val adapter = RepositoryAdapter(object : RepositoryAdapter.ItemClickListener {
            override fun onItemClick(repository: Repository) {
                mPresenter.onRepositoryClicked(repository)
            }
        })
        mRecyclerView!!.adapter = adapter
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
    }

    override fun showRepositories(repositoryList: List<Repository>) {
        val adapter = mRecyclerView!!.adapter as RepositoryAdapter
        adapter.addRepositories(repositoryList)
    }

    override fun showProgress() {
        mFloatingActionButton!!.isEnabled = false
        mRecyclerView!!.visibility = View.GONE
        mProgressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mFloatingActionButton!!.isEnabled = true
        mRecyclerView!!.visibility = View.VISIBLE
        mProgressBar!!.visibility = View.GONE
    }

    override fun showInfoMessage(message: String) {
        Snackbar.make(mRecyclerView!!, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showErrorMessage() {
        Snackbar.make(mRecyclerView!!, "Error", Snackbar.LENGTH_SHORT).show()
    }

    @OnClick(R.id.fab)
    internal fun onClickFab(view: View) {
        mPresenter.fetchRepositories()
    }

    override fun getContext(): Context {
        return this@RepositoryActivity
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

}
