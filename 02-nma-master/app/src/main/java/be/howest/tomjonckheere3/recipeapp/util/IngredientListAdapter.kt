package be.howest.tomjonckheere3.recipeapp.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.howest.tomjonckheere3.recipeapp.databinding.ListItemIngredientBinding
import be.howest.tomjonckheere3.recipeapp.domain.Ingredient

class IngredientListAdapter: ListAdapter<Ingredient, IngredientListAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ListItemIngredientBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Ingredient) {
            binding.ingredient = item
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Ingredient>() {
        override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.ingredient == newItem.ingredient
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(ListItemIngredientBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = getItem(position)
        holder.bind(ingredient)
    }

}