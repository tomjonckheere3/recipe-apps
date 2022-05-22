package be.howest.tomjonckheere3.recipeapp.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.howest.tomjonckheere3.recipeapp.databinding.ListItemCommentBinding
import be.howest.tomjonckheere3.recipeapp.domain.Comment

class CommentListAdapter: ListAdapter<Comment, CommentListAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ListItemCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Comment) {
            binding.comment = item
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Comment>() {
        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.userId == newItem.userId && oldItem.commentContent == oldItem.commentContent
        }

        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem === newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(ListItemCommentBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }
}