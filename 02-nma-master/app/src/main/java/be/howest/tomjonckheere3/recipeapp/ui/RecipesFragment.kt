package be.howest.tomjonckheere3.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import be.howest.tomjonckheere3.recipeapp.databinding.FragmentRecipesBinding
import be.howest.tomjonckheere3.recipeapp.util.RecipeListAdapter
import be.howest.tomjonckheere3.recipeapp.viewmodels.RecipesViewModel

class RecipesFragment : Fragment() {

    private val viewModel: RecipesViewModel by lazy {
        ViewModelProvider(this).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentRecipesBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.addButton.setOnClickListener {
            this.findNavController().navigate(
                    RecipesFragmentDirections.actionRecipesFragmentToAddRecipeFragment()
            )
        }

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        binding.recipes.adapter = RecipeListAdapter(RecipeListAdapter.OnClickListener {
            viewModel.displayRecipeDetails(it)
        })

        viewModel.navigateToSelectedRecipe.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                        RecipesFragmentDirections.actionRecipesFragmentToDetailedRecipeFragment(it))
                viewModel.displayRecipeDetailsComplete()
            }
        })

        return binding.root
    }
}