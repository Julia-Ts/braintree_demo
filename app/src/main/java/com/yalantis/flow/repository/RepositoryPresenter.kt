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
        view?.showProgress()
        fetchRepositories(false)
    }

    private fun fetchRepositories(local: Boolean) {
        addDisposable(mRepository.getRepositories(ORGANIZATION_NAME, local).subscribe({ repositories ->
            view?.hideProgress()
            view?.showRepositories(repositories)
        }, { throwable ->
            view?.hideProgress()
            view?.showError(throwable.message)
        }))
    }

    override fun onRepositoryClicked(repository: Repository) {
        view?.showMessage("Repository has " + repository.starsCount + " stars.")
    }

    override fun detachView() {
        super.detachView()
        mRepository.clear()
    }
}
