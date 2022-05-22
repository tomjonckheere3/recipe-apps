package be.howest.tomjonckheere3.recipeapp.util

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.howest.tomjonckheere3.recipeapp.databinding.ListItemRecipeBinding
import be.howest.tomjonckheere3.recipeapp.domain.NormalRecipe
import be.howest.tomjonckheere3.recipeapp.network.Recipe

class RecipeListAdapter(private val onClickListener: OnClickListener): ListAdapter<NormalRecipe, RecipeListAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ListItemRecipeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: NormalRecipe) {
            binding.recipe = item
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<NormalRecipe>() {
        override fun areItemsTheSame(oldItem: NormalRecipe, newItem: NormalRecipe): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: NormalRecipe, newItem: NormalRecipe): Boolean {
            return oldItem.recipeId == newItem.recipeId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(ListItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(recipe)
        }
        holder.bind(recipe)
    }

    class OnClickListener(val clickListener: (recipe: NormalRecipe) -> Unit) {
        fun onClick(recipe: NormalRecipe) = clickListener(recipe)
    }
}