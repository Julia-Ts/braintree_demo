package com.yalantis.flow.repository

import com.yalantis.base.BaseMvpPresenter
import com.yalantis.base.BaseMvpView
import com.yalantis.data.Repository

class RepositoryContract {

    internal interface Presenter : BaseMvpPresenter<View> {

        fun initRepositories()

        fun fetchRepositories()

        fun onRepositoryClicked(repository: Repository)

    }

    interface View : BaseMvpView {

        fun showRepositories(repositoryList: List<Repository>)

        override fun showProgress()

        override fun hideProgress()

        fun showInfoMessage(message: String)

        fun showErrorMessage()

    }

}
