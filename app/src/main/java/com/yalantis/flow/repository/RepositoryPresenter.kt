package com.yalantis.flow.repository

import com.yalantis.base.BaseMvpPresenterImpl
import com.yalantis.data.Repository
import com.yalantis.data.source.repository.ReposRepository

class RepositoryPresenter : BaseMvpPresenterImpl<RepositoryContract.View>(), RepositoryContract.Presenter {

    private var mRepository: ReposRepository? = null

    override fun attachView(view: RepositoryContract.View) {
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
        addSubscription(mRepository!!.getRepositories(ORGANIZATION_NAME, local)
                .subscribe({ repositories ->
                    mView?.hideProgress()
                    mView?.showRepositories(repositories)
                }) {
                    mView?.hideProgress()
                    mView?.showErrorMessage()
                })
    }

    override fun onRepositoryClicked(repository: Repository) {
        mView?.showInfoMessage("Repository has " + repository.starsCount + " stars.")
    }

    companion object {

        private val ORGANIZATION_NAME = "Yalantis"
    }

}
