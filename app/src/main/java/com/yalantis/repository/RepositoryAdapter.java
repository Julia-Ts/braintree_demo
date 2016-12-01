package com.yalantis.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yalantis.R;
import com.yalantis.model.Repository;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {

    private final ItemClickListener mItemClickListener;

    private List<Repository> mRepositories = new ArrayList<>();

    RepositoryAdapter(@Nullable ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    void addRepositories(List<Repository> repositoryList) {
        mRepositories.clear();
        mRepositories.addAll(repositoryList);
        notifyDataSetChanged();
    }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repository, parent, false);
        return new RepositoryViewHolder(itemView, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        Repository repository = mRepositories.get(position);
        holder.bindData(repository);
    }

    @Override
    public int getItemCount() {
        return mRepositories.size();
    }

    interface ItemClickListener {
        void onItemClick(Repository repository);
    }

    static class RepositoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemClickListener mItemClickListener;
        @Bind(R.id.text_view_title)
        TextView titleTextView;
        @Bind(R.id.text_view_description)
        TextView descriptionTextView;
        private Repository mRepository;

        RepositoryViewHolder(@NonNull View itemView, @Nullable ItemClickListener itemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mItemClickListener = itemClickListener;
        }

        void bindData(Repository repository) {
            mRepository = repository;

            titleTextView.setText(repository.getName());
            descriptionTextView.setText(repository.getDescription());
        }

        @OnClick(R.id.linear_layout_content)
        void onClickListItem() {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(mRepository);
            }
        }

    }

}