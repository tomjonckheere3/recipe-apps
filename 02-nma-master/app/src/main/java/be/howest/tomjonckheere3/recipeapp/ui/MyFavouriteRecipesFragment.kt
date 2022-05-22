package be.howest.tomjonckheere3.recipeapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import be.howest.tomjonckheere3.recipeapp.databinding.FragmentMyFavouritesRecipesBinding
import be.howest.tomjonckheere3.recipeapp.util.RecipeListAdapter
import be.howest.tomjonckheere3.recipeapp.viewmodels.MyFavouriteRecipesViewModel

class MyFavouriteRecipesFragment: Fragment() {

    private val viewModel: MyFavouriteRecipesViewModel by lazy {
        ViewModelProvider(this).get(MyFavouriteRecipesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentMyFavouritesRecipesBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.favouriteRecipes.adapter = RecipeListAdapter(RecipeListAdapter.OnClickListener {
            viewModel.displayRecipeDetails(it)
        })

        viewModel.navigateToSelectedRecipe.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                        MyFavouriteRecipesFragmentDirections.actionMyFavouriteRecipesFragment2ToDetailedRecipeFragment(it))
                viewModel.displayRecipeDetailsComplete()
            }
        })

        return binding.root
    }

}