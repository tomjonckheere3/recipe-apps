package be.howest.tomjonckheere3.recipeapp.viewmodels

import android.app.Application
import android.text.Editable
import android.util.Log
import androidx.lifecycle.*
import be.howest.tomjonckheere3.recipeapp.database.getDatabase
import be.howest.tomjonckheere3.recipeapp.domain.Credentials
import be.howest.tomjonckheere3.recipeapp.repository.RecipesRepository
import be.howest.tomjonckheere3.recipeapp.repository.UsersRepository
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val usersRepository = UsersRepository(getDatabase(application))

    init{
        RecipesRepository(getDatabase(application))
    }

    private var _navigateToSuggestions = MutableLiveData<Boolean>(false)

    val navigateToSuggestions: LiveData<Boolean>
        get() = _navigateToSuggestions

    private var _isFormFilledIn = MutableLiveData<Boolean>(true)

    val isFormFilledIn: LiveData<Boolean>
        get() = _isFormFilledIn

    private var _doesUserExist = MutableLiveData<Boolean>(true)

    val doesUserExist: LiveData<Boolean>
        get() = _doesUserExist

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            try {
                if (validateInput(username, password)) {
                    val credentials = Credentials(username, password)
                    if(usersRepository.loginUser(credentials)) {
                        _navigateToSuggestions.value = true
                    } else {
                        _doesUserExist.value = false
                    }
                }
            } catch (exception: Exception) {
                exception.message?.let { Log.e("error", it) }
            }
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username == "" || password == "") {
            _isFormFilledIn.value = false
            return false
        }
        return true
    }

    private fun doneNavigating() {
        _navigateToSuggestions.value = false
    }
}