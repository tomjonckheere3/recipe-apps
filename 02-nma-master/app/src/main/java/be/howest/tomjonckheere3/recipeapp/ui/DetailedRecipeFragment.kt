package be.howest.tomjonckheere3.recipeapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import be.howest.tomjonckheere3.recipeapp.databinding.FragmentDetailedRecipeBinding
import be.howest.tomjonckheere3.recipeapp.util.CommentListAdapter
import be.howest.tomjonckheere3.recipeapp.util.IngredientListAdapter
import be.howest.tomjonckheere3.recipeapp.util.StepListAdapter
import be.howest.tomjonckheere3.recipeapp.viewmodels.DetailedRecipeViewModel
import be.howest.tomjonckheere3.recipeapp.viewmodels.factories.DetailedRecipeViewModelFactory

class DetailedRecipeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application

        val binding = FragmentDetailedRecipeBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        val recipeId = DetailedRecipeFragmentArgs.fromBundle(requireArguments()).selectedRecipeId

        val viewModelFactory = DetailedRecipeViewModelFactory(recipeId, application)

        val viewModel = ViewModelProvider(
                this, viewModelFactory).get(DetailedRecipeViewModel::class.java)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        binding.ingredients.adapter = IngredientListAdapter()
        binding.steps.adapter = StepListAdapter()
        binding.comments.adapter = CommentListAdapter()

        binding.addComment.setOnClickListener {
            val commentContent = binding.commentInput.text.toString()
            viewModel.addComment(commentContent, recipeId)
        }

        viewModel.addedComment.observe(viewLifecycleOwner, Observer<Int> {
            if (it == 1) {
                binding.commentInput.text.clear()
                viewModel.showedAddCommentResponse()
                viewModel.getDetailedRecipe(recipeId)
            } else if (it == 0) {
                viewModel.showedAddCommentResponse()
            }
        })

        binding.addFavourite.setOnClickListener {
            viewModel.addFavourite(recipeId)
        }

        viewModel.addedFavourite.observe(viewLifecycleOwner, Observer {
            binding.addFavourite.visibility = View.INVISIBLE
            binding.favouriteMessage.text = viewModel.addedFavourite.value
            binding.favouriteMessage.visibility = View.VISIBLE
        })

        return binding.root
    }
}