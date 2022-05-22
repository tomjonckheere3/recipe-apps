package be.howest.tomjonckheere3.recipeapp.viewmodels;

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import be.howest.tomjonckheere3.recipeapp.database.getDatabase
import be.howest.tomjonckheere3.recipeapp.domain.CommentData
import be.howest.tomjonckheere3.recipeapp.domain.FavouriteData
import be.howest.tomjonckheere3.recipeapp.repository.RecipesRepository
import be.howest.tomjonckheere3.recipeapp.repository.UsersRepository
import kotlinx.coroutines.launch

class DetailedRecipeViewModel(recipeId: Int, application: Application) : AndroidViewModel(application) {

    private val recipesRepository = RecipesRepository(getDatabase(application))
    private val usersRepository = UsersRepository(getDatabase(application))

    val detailedRecipe = recipesRepository.detailedRecipe

    private var _addedComment = MutableLiveData<Int>(2)

    val addedComment: LiveData<Int>
        get() = _addedComment

    private var _addedFavourite = MutableLiveData<String?>()

    val addedFavourite: LiveData<String?>
        get() = _addedFavourite

    init {
        getDetailedRecipe(recipeId)
    }

    fun addComment(commentContent: String, recipeId: Int) {
        viewModelScope.launch {
            try {
                if (validateInput(commentContent)) {
                    val comment = CommentData(null, commentContent)
                    if (recipesRepository.addComment(recipeId, comment) != -1) {
                        _addedComment.value = 1
                    } else {
                        _addedComment.value = 0
                    }
                }
            } catch (exception: Exception) {
                exception.message?.let { Log.e("DetailedRecipeViewModel", it) }
            }
        }
    }

    fun addFavourite(recipeId: Int) {
        viewModelScope.launch {
            try {
                val favouriteData = FavouriteData(recipeId)
                val response = usersRepository.addFavouriteRecipe(favouriteData)
                if (response != null) {
                    if (!response.success.isNullOrEmpty()) {
                        _addedFavourite.value = response.success
                    } else if (!response.error.isNullOrEmpty()) {
                        _addedFavourite.value = response.error
                    }
                    else {
                        _addedFavourite.value = "Something went wrong"
                    }
                }
            } catch (exception: Exception) {
                exception.message?.let { Log.e("DetailedRecipeViewModel", it) }
            }
        }
    }

    fun getDetailedRecipe(recipeId: Int) {
        viewModelScope.launch {
            try {
                recipesRepository.getDetailedRecipe(recipeId)
            } catch (exception: Exception) {
                exception.message?.let { Log.e("DetailedRecipeViewModel", it) }
            }
        }
    }

    private fun validateInput(commentContent: String) : Boolean {
        if (commentContent == "") {
            return false
        }
        return true
    }

    fun showedAddCommentResponse() {
        _addedComment.value = 2
    }
}
