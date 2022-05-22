package be.howest.tomjonckheere3.recipeapp.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.howest.tomjonckheere3.recipeapp.domain.NormalRecipe
import be.howest.tomjonckheere3.recipeapp.viewmodels.DetailedRecipeViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class DetailedRecipeViewModelFactory(
        private val recipeId: Int,
        private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailedRecipeViewModel::class.java)) {
            return DetailedRecipeViewModel(recipeId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}