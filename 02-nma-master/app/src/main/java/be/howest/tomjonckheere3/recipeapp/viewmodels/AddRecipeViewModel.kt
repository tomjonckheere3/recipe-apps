package be.howest.tomjonckheere3.recipeapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import be.howest.tomjonckheere3.recipeapp.database.getDatabase
import be.howest.tomjonckheere3.recipeapp.domain.Ingredient
import be.howest.tomjonckheere3.recipeapp.domain.NormalRecipe
import be.howest.tomjonckheere3.recipeapp.domain.RecipeData
import be.howest.tomjonckheere3.recipeapp.domain.Step
import be.howest.tomjonckheere3.recipeapp.repository.RecipesRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class AddRecipeViewModel(application: Application): AndroidViewModel(application) {

    private val recipesRepository = RecipesRepository(getDatabase(application))

    private var _navigateToAddedRecipe = MutableLiveData<Int>()

    val navigateToAddedRecipe: LiveData<Int>
        get() = _navigateToAddedRecipe

    private var _isFormFilledIn = MutableLiveData<Boolean>(true)

    val isFormFilledIn: LiveData<Boolean>
        get() = _isFormFilledIn

    fun addRecipe(recipeName: String, mealType: String, servings: Int, prepareTime: Int,
                  price: Int, description: String, base64Image: String?, ingredients: List<String>,
                  steps: Map<Int, String>) {
        viewModelScope.launch {
            try {
                val ingredientsList = getIngredients(ingredients)
                val stepsList = getSteps(steps)
                if (validateInput(recipeName, mealType, servings, prepareTime, price,
                                description, base64Image, ingredientsList, stepsList)) {

                    val recipeData = RecipeData(recipeName = recipeName, mealType = mealType,
                            servings = servings, prepareTime = prepareTime, price = price ,
                            description = description, image = base64Image,
                            ingredients = ingredientsList, steps = stepsList, userId = null)

                    val recipeId = recipesRepository.addRecipe(recipeData);
                    displayRecipeDetails(recipeId)
                }
            } catch (exception: Exception) {
                exception.message?.let { Log.e("error", it) }
            }
        }

    }

    private fun getIngredients(stringIngredients: List<String>): MutableList<Ingredient> {
        val ingredientsList = emptyList<Ingredient>().toMutableList()
        for (stringIngredient in stringIngredients) {
            val ingredient = Ingredient(ingredient = stringIngredient)
            ingredientsList += ingredient
        }
        return ingredientsList
    }

    private fun getSteps(stepEntries: Map<Int, String>): MutableList<Step> {
        val stepsList = emptyList<Step>().toMutableList()
        for (stepEntry in stepEntries) {
            val step = Step(step = stepEntry.key, direction = stepEntry.value)
            stepsList += step
        }
        return stepsList
    }

    private fun validateInput(recipeName: String, mealType: String, servings: Int, prepareTime: Int,
                              price: Int, description: String, base64Image: String?,
                              ingredients: List<Ingredient>, steps: List<Step>) : Boolean {

        if (recipeName == "" || mealType == "" || servings <= 0 || prepareTime <= 0 || price <= 0 ||
                description == "" || base64Image == "" || ingredients.isEmpty() || steps.isEmpty()) {

            _isFormFilledIn.value = false
            return false
        }
        return true
    }

    fun displayRecipeDetails(recipeId: Int) {
        _navigateToAddedRecipe.value = recipeId
    }
    fun displayRecipeDetailsComplete() {
        _navigateToAddedRecipe.value = null
    }
}