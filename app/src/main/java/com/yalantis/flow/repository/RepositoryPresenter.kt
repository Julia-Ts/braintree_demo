package com.yalantis.flow.repository

import com.yalantis.base.BasePresenterImplementation
import com.yalantis.data.Repository
import com.yalantis.data.source.repository.ReposRepository

class RepositoryPresenter : BasePresenterImplementation<RepositoryContract.View>(), RepositoryContract.Presenter {

    private val ORGANIZATION_NAME = "Yalantis"
    private val mRepository: ReposRepository = ReposRepository()

    override fun initRepositories() {
        fetchRepositories(true)
    }

    override fun fetchRepositories() {
        mView?.showProgress()
        fetchRepositories(false)
    }

    private fun fetchRepositories(local: Boolean) {
        addSubscription(mRepository.getRepositories(ORGANIZATION_NAME, local).subscribe({ repositories ->
            mView?.hideProgress()
            mView?.showRepositories(repositories)
        }, { throwable ->
            mView?.hideProgress()
            mView?.showError(throwable.message)
        }))
    }

    override fun onRepositoryClicked(repository: Repository) {
        mView?.showMessage("Repository has " + repository.starsCount + " stars.")
    }

    override fun detachView() {
        super.detachView()
        mRepository.clear()
    }
}
