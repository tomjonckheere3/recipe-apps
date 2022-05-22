package be.howest.tomjonckheere3.recipeapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import be.howest.tomjonckheere3.recipeapp.database.getDatabase
import be.howest.tomjonckheere3.recipeapp.domain.NormalRecipe
import be.howest.tomjonckheere3.recipeapp.repository.UsersRepository
import kotlinx.coroutines.launch

class MyRecipesViewModel(application: Application): AndroidViewModel(application) {

    private val usersRepository = UsersRepository(getDatabase(application))

    val myRecipes = usersRepository.myRecipes

    private val _navigateToSelectedRecipe = MutableLiveData<Int>()

    val navigateToSelectedRecipe: LiveData<Int>
        get() = _navigateToSelectedRecipe

    init {
        refreshMyRecipes()
    }

    private fun refreshMyRecipes() {
        viewModelScope.launch {
            try {
                usersRepository.refreshMyRecipes()
            } catch (exception: Exception) {
                exception.message?.let { Log.e("MyRecipesViewModel", it) }
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