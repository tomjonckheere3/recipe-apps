package be.howest.tomjonckheere3.recipeapp.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import be.howest.tomjonckheere3.recipeapp.database.DatabaseUser
import be.howest.tomjonckheere3.recipeapp.database.RecipesDatabase
import be.howest.tomjonckheere3.recipeapp.database.asNormalRecipes
import be.howest.tomjonckheere3.recipeapp.domain.*
import be.howest.tomjonckheere3.recipeapp.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import timber.log.Timber
import java.lang.Exception
import kotlin.math.log

class UsersRepository(private val database: RecipesDatabase) {

    val myRecipes: LiveData<List<NormalRecipe>> = Transformations.map(database.myRecipeDao.getMyRecipes()) {
        it.asNormalRecipes()
    }

    val myFavouriteRecipes: LiveData<List<NormalRecipe>> = Transformations.map(database.favouritesDao.getFavouriteRecipes()) {
        it.asNormalRecipes()
    }

    suspend fun loginUser(credentials: Credentials) : Boolean{
        var doesUserExist = true
        withContext(Dispatchers.IO) {
            try {
                val loginResponse: LoginResponse = RecipeApi.retrofitService.loginUser(credentials)
                if (loginResponse.jwt == null) {
                    doesUserExist = false
                } else {
                    emptyRoomDatabases()
                    getCurrentUser(loginResponse)
                }
            } catch (exception: Exception) {
                Timber.d(exception.stackTrace.toString())
            }
        }
        return doesUserExist
    }

    suspend fun addUser(registerData: RegisterData) {
        withContext(Dispatchers.IO) {
            try {
                val loginResponse: LoginResponse = RecipeApi.retrofitService.addUser(registerData)
                getCurrentUser(loginResponse)
            } catch (exception: Exception) {
                exception.message?.let { Log.e("UserRepo: addUser", it) }
            }
        }
    }

    private suspend fun getCurrentUser(loginResponse: LoginResponse) {
        val token = "Bearer ${loginResponse.jwt}"
        val loggedInUser: User = RecipeApi.retrofitService.getUser(token, loginResponse.userId)
        val databaseCurrentUser: DatabaseUser = loggedInUser.asDatabaseModel(token)
        database.userDao.insertCurrentUser(databaseCurrentUser)
    }

    suspend fun refreshFavouriteRecipes() {
        withContext(Dispatchers.IO) {
            val currentUser = database.userDao.getCurrentUser()
            val favouriteRecipesList = RecipeApi.retrofitService.getFavouritesOfUser(currentUser.token, currentUser.userId)
            database.favouritesDao.insertFavouriteRecipes(favouriteRecipesList.asDatabaseFavouriteRecipe())
        }
    }

    suspend fun addFavouriteRecipe(favouriteData: FavouriteData): AddFavouriteResponse? {
        var response: AddFavouriteResponse? = null
        withContext(Dispatchers.IO) {
            val currentUser = database.userDao.getCurrentUser()
            response = RecipeApi.retrofitService.addFavourite(currentUser.token, currentUser.userId, favouriteData)
        }
        return response
    }

    suspend fun refreshMyRecipes() {
        withContext(Dispatchers.IO) {
            val currentUser = database.userDao.getCurrentUser()
            val myRecipesList = RecipeApi.retrofitService.getRecipesOfUser(currentUser.token, currentUser.userId)
            database.myRecipeDao.insertMyRecipes(myRecipesList.asDatabaseMyRecipe())
        }
    }

    private fun emptyRoomDatabases() {
        database.userDao.deleteCurrentUser()
        database.myRecipeDao.deleteMyRecipes()
        database.favouritesDao.deleteFavourites()
    }
}