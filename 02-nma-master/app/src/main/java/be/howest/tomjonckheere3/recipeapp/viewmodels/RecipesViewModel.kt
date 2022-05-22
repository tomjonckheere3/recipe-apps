package be.howest.tomjonckheere3.recipeapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import be.howest.tomjonckheere3.recipeapp.network.RecipeApi
import be.howest.tomjonckheere3.recipeapp.database.getDatabase
import be.howest.tomjonckheere3.recipeapp.domain.NormalRecipe
import be.howest.tomjonckheere3.recipeapp.network.Recipe
import be.howest.tomjonckheere3.recipeapp.repository.RecipesRepository
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.ArrayList

class RecipesViewModel(application: Application): AndroidViewModel(application) {

    private val recipesRepository = RecipesRepository(getDatabase(application))

    val recipes = recipesRepository.normalRecipes

    private val _navigateToSelectedRecipe = MutableLiveData<Int>()

    val navigateToSelectedRecipe: LiveData<Int>
        get() = _navigateToSelectedRecipe

    init {
        refreshRecipes()
    }

    private fun refreshRecipes() {
        viewModelScope.launch {
            try {
                recipesRepository.refreshRecipes()
            } catch (exception: Exception) {
                exception.message?.let { Log.e("RecipesViewModel", it) }
            }
        }
    }

    fun displayRecipeDetails(recipe: NormalRecipe) {
        _navigateToSelectedRecipe.value = recipe.recipeId
    }
    fun displayRecipeDetailsComplete() {
        _navigateToSelectedRecipe.value = null
    }
}