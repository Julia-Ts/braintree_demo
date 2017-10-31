package com.yalantis.flow.repository

import com.yalantis.base.BasePresenter
import com.yalantis.base.BaseView
import com.yalantis.data.Repository

class RepositoryContract {

    interface Presenter : BasePresenter {

        fun initRepositories()

        fun fetchRepositories()

        fun onRepositoryClicked(repository: Repository)

    }

    interface View : BaseView {

        fun showRepositories(repositoryList: List<Repository>)
    }

}
