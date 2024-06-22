package com.bykamri.submission.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bykamri.submission.R
import com.bykamri.submission.databinding.ItemUserBinding
import com.bykamri.submission.server.model.GithubUser

class SearchUserAdapter : RecyclerView.Adapter<SearchUserAdapter.ListViewHolder>() {
    private val listUser = ArrayList<GithubUser>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GithubUser) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(ivAvatar)
                tvUsername.text = user.login
                tvUrl.text = user.htmlUrl
                chipCategoryText.text = user.type
                if (user.type == "User") {
                    chipCategory.backgroundTintList =
                        AppCompatResources.getColorStateList(itemView.context, R.color.colorInfo)
                }
            }
        }
    }

    fun setData(items: ArrayList<GithubUser>) {
        val isDifferent = DiffUtil.calculateDiff(DiffUtilCallback(listUser, items))
        listUser.clear()
        listUser.addAll(items)
        isDifferent.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun interface OnItemClickCallback {
        fun onItemClicked(data: GithubUser)
    }

    class DiffUtilCallback(private val oldList: List<GithubUser>, private val newList: List<GithubUser>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem == newItem
        }
    }

}
