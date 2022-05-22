package be.howest.tomjonckheere3.recipeapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import be.howest.tomjonckheere3.recipeapp.databinding.FragmentMyRecipesBinding
import be.howest.tomjonckheere3.recipeapp.util.RecipeListAdapter
import be.howest.tomjonckheere3.recipeapp.viewmodels.MyFavouriteRecipesViewModel
import be.howest.tomjonckheere3.recipeapp.viewmodels.MyRecipesViewModel

class MyRecipesFragment: Fragment() {

    private val viewModel: MyRecipesViewModel by lazy {
        ViewModelProvider(this).get(MyRecipesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentMyRecipesBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.myRecipes.adapter = RecipeListAdapter(RecipeListAdapter.OnClickListener {
            viewModel.displayRecipeDetails(it)
        })

        viewModel.navigateToSelectedRecipe.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                        MyRecipesFragmentDirections.actionMyRecipesFragment2ToDetailedRecipeFragment(it))
                viewModel.displayRecipeDetailsComplete()
            }
        })

        return binding.root
    }

}