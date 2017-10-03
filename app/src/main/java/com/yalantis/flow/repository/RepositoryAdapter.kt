package com.yalantis.flow.repository

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yalantis.R
import com.yalantis.data.Repository
import java.util.*

internal class RepositoryAdapter(val onClick: (Repository) -> Unit) : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    private val mRepositories = ArrayList<Repository>()

    fun addRepositories(repositoryList: List<Repository>) {
        mRepositories.clear()
        mRepositories.addAll(repositoryList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding: View = LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = mRepositories[position]
    }

    override fun getItemCount(): Int {
        return mRepositories.size
    }

    internal class RepositoryViewHolder(val binding: View, val onClick: (Repository) -> Unit) : RecyclerView.ViewHolder(binding) {

        fun bindData(repository: Repository) {
            with(repository) {
//                text_view_title.text = name
//                text_view_description.text = description
                binding.setOnClickListener { onClick(this) }
            }
        }

    }
}