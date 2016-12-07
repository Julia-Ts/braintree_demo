package com.yalantis.flow.repository

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yalantis.R
import com.yalantis.data.Repository
import com.yalantis.databinding.ItemRepositoryBinding
import java.util.*

internal class RepositoryAdapter(val repoClick: (Repository) -> Unit) : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    private val mRepositories = ArrayList<Repository>()

    fun addRepositories(repositoryList: List<Repository>) {
        mRepositories.clear()
        mRepositories.addAll(repositoryList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding: ItemRepositoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_repository, parent, false)
        return RepositoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = mRepositories[position]
        holder.binding.repo = repository
        holder.binding.root.setOnClickListener { repoClick(repository) }
    }

    override fun getItemCount(): Int {
        return mRepositories.size
    }

    internal class RepositoryViewHolder(val binding: ItemRepositoryBinding) : RecyclerView.ViewHolder(binding.root)
}