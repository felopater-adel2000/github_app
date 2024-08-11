package com.felo.github_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.felo.github_app.data.model.RepositoryModel
import com.felo.github_app.databinding.ItemLoadingBinding
import com.felo.github_app.databinding.ItemRepositoryBinding
import com.felo.github_app.databinding.ItemRetryBinding

class RepositoryAdapter(
    private val requestManager: RequestManager,
    private val callback: RepositoryItemCallback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "RepositoryAdapter"
    private val DIFF_CALLBACK =
        object : DiffUtil.ItemCallback<RepositoryModel>() {
            override fun areItemsTheSame(
                oldItem: RepositoryModel,
                newItem: RepositoryModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RepositoryModel,
                newItem: RepositoryModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    private val differ = AsyncListDiffer(
        CustomRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    inner class CustomRecyclerChangeCallback(private val adapter: RepositoryAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            LOADING_TYPE -> {
                LoadingItemViewHolder(ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }

            ERROR_TYPE -> {
                RetryViewHolder(ItemRetryBinding.inflate(LayoutInflater.from(parent.context), parent, false), callback)
            }

            else -> {
                CustomItemViewHolder(
                    ItemRepositoryBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    requestManager,
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CustomItemViewHolder -> {
                holder.bind(differ.currentList[position])
            }
            is RetryViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    fun submitList(list: List<RepositoryModel>) {
        differ.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            differ.currentList[position].isLoading -> LOADING_TYPE
            differ.currentList[position].isError -> ERROR_TYPE
            else -> ITEM_TYPE
        }
    }


    class CustomItemViewHolder(
        private val binding: ItemRepositoryBinding,
        private val requestManager: RequestManager,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RepositoryModel) = with(binding.root) {
            binding.apply {
                requestManager.load(item.owner?.avatarUrl).into(ivUserAvatar)
                tvRepoName.text = item.name
            }
        }
    }

    class RetryViewHolder(
        private val binding: ItemRetryBinding,
        private val callback: RepositoryItemCallback
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RepositoryModel) = with(binding.root) {
            binding.apply {
                tvError.text = item.errorMessage
                root.setOnClickListener {
                    callback.onRetryClick()
                }
            }
        }
    }

    class LoadingItemViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
    {}

    interface RepositoryItemCallback {
        fun onRetryClick()
    }

    companion object {
        const val ITEM_TYPE = 0
        const val LOADING_TYPE = 1
        const val ERROR_TYPE = 2
    }

}