package com.yalantis.flow.repository

import com.yalantis.base.BasePresenterImplementation
import com.yalantis.base.BaseView
import com.yalantis.data.Repository
import com.yalantis.data.source.repository.ReposRepository

class RepositoryPresenter : BasePresenterImplementation<RepositoryContract.View>(), RepositoryContract.Presenter {

    private val ORGANIZATION_NAME = "Yalantis"
    private var mRepository: ReposRepository? = null

    override fun attachView(view: BaseView) {
        super.attachView(view)
        mRepository = ReposRepository(view.getContext())
    }

    override fun initRepositories() {
        fetchRepositories(true)
    }

    override fun fetchRepositories() {
        mView?.showProgress()
        fetchRepositories(false)
    }

    private fun fetchRepositories(local: Boolean) {
        mRepository?.getRepositories(ORGANIZATION_NAME, local)?.subscribe({ repositories ->
            mView?.hideProgress()
            mView?.showRepositories(repositories)
        }, { throwable ->
            mView?.hideProgress()
            mView?.showError(throwable.message)
        })?.let { addSubscription(it) }
    }

    override fun onRepositoryClicked(repository: Repository) {
        mView?.showMessage("Repository has " + repository.starsCount + " stars.")
    }
}
