package com.yalantis.flow.repository

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import butterknife.OnClick
import com.yalantis.R
import com.yalantis.data.Repository
import java.util.*

internal class RepositoryAdapter(private val mItemClickListener: RepositoryAdapter.ItemClickListener?) : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    private val mRepositories = ArrayList<Repository>()

    fun addRepositories(repositoryList: List<Repository>) {
        mRepositories.clear()
        mRepositories.addAll(repositoryList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(itemView, mItemClickListener)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = mRepositories[position]
        holder.bindData(repository)
    }

    override fun getItemCount(): Int {
        return mRepositories.size
    }

    internal interface ItemClickListener {
        fun onItemClick(repository: Repository)
    }

    internal class RepositoryViewHolder(itemView: View, private val mItemClickListener: ItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        @Bind(R.id.text_view_title)
        var titleTextView: TextView? = null
        @Bind(R.id.text_view_description)
        var descriptionTextView: TextView? = null
        private var mRepository: Repository? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindData(repository: Repository) {
            mRepository = repository

            titleTextView!!.text = repository.name
            descriptionTextView!!.text = repository.description
        }

        @OnClick(R.id.linear_layout_content)
        fun onClickListItem() {
            mRepository?.let {
                mItemClickListener?.onItemClick(mRepository!!)
            }
        }

    }

}