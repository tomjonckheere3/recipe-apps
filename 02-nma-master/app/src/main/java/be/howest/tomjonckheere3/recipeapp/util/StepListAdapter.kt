package be.howest.tomjonckheere3.recipeapp.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.howest.tomjonckheere3.recipeapp.databinding.ListItemRecipeBinding
import be.howest.tomjonckheere3.recipeapp.databinding.ListItemStepBinding
import be.howest.tomjonckheere3.recipeapp.domain.Step

class StepListAdapter: ListAdapter<Step, StepListAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ListItemStepBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Step) {
            binding.step = item
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Step>() {
        override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
            return oldItem.step == newItem.step && oldItem.direction == oldItem.direction
        }

        override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
            return oldItem === newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(ListItemStepBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val step = getItem(position)
        holder.bind(step)
    }

}