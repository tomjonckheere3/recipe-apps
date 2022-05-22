package be.howest.tomjonckheere3.recipeapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.howest.tomjonckheere3.recipeapp.database.RecipesDatabase
import be.howest.tomjonckheere3.recipeapp.database.asNormalRecipe
import be.howest.tomjonckheere3.recipeapp.database.asNormalRecipes
import be.howest.tomjonckheere3.recipeapp.domain.*
import be.howest.tomjonckheere3.recipeapp.network.RecipeApi
import be.howest.tomjonckheere3.recipeapp.network.asDatabaseModel
import be.howest.tomjonckheere3.recipeapp.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.lang.Exception

class RecipesRepository(private val database: RecipesDatabase) {

    val normalRecipes: LiveData<List<NormalRecipe>> = Transformations.map(database.recipeDao.getRecipes()) {
        it.asNormalRecipes()
    }

    var suggestedNormalRecipes: MutableLiveData<SuggestedNormalRecipes> = MutableLiveData()

    var detailedRecipe: MutableLiveData<DetailedRecipe> = MutableLiveData()

    suspend fun refreshRecipes() {
        withContext(Dispatchers.IO) {
            val recipeList = RecipeApi.retrofitService.getRecipes()
            database.recipeDao.insertRecipes(recipeList.asDatabaseModel())
        }
    }

    suspend fun refreshSuggestions() {
        withContext(Dispatchers.IO) {
            val breakfast = database.recipeDao.getRandomBreakfastRecipe().asNormalRecipe()
            val supper = database.recipeDao.getRandomSupperRecipe().asNormalRecipe()
            val dinner = database.recipeDao.getRandomDinnerRecipe().asNormalRecipe()
            suggestedNormalRecipes.postValue(SuggestedNormalRecipes(breakfast, supper, dinner))
        }
    }

    suspend fun getDetailedRecipe(recipeId: Int) {
        withContext(Dispatchers.IO) {
            detailedRecipe.postValue(RecipeApi.retrofitService.getRecipeById(recipeId).asDomainModel())
        }
    }

    suspend fun addRecipe(recipeData: RecipeData): Int {
        var addedRecipe = -1
        withContext(Dispatchers.IO) {
            try {
                val user = database.userDao.getCurrentUser()
                recipeData.userId = user.userId
                val response: AddRecipeResponse = RecipeApi.retrofitService.addRecipe(user.token, recipeData)
                addedRecipe = response.recipeId
            } catch (exception: Exception) {
                exception.message?.let { Log.e("RecipeRepo: addRecipe", it) }
            }
        }
        return addedRecipe
    }

    suspend fun addComment(recipeId: Int, commentData: CommentData): Int {
        var addedComment = -1
        withContext(Dispatchers.IO) {
            try {
                val user = database.userDao.getCurrentUser()
                commentData.userId = user.userId
                val response = RecipeApi.retrofitService.addComment(user.token, recipeId, commentData)
                addedComment = response.commentId
            } catch (exception: Exception) {
                exception.message?.let { Log.e("RecipeRepo: addRecipe", it) }
            }
        }
        return addedComment
    }
}