package be.howest.tomjonckheere3.recipeapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import be.howest.tomjonckheere3.recipeapp.database.getDatabase
import be.howest.tomjonckheere3.recipeapp.domain.RegisterData
import be.howest.tomjonckheere3.recipeapp.repository.UsersRepository
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application): AndroidViewModel(application) {

    private val usersRepository = UsersRepository(getDatabase(application))

    private var _navigateToSuggestions = MutableLiveData<Boolean>(false)

    val navigateToSuggestions: LiveData<Boolean>
        get() = _navigateToSuggestions

    private var _isFormFilledIn = MutableLiveData<Boolean>(true)

    val isFormFilledIn: LiveData<Boolean>
        get() = _isFormFilledIn

    private var _arePasswordsEqual = MutableLiveData<Boolean>(true)

    val arePasswordsEqual: LiveData<Boolean>
        get() = _arePasswordsEqual

    fun registerUser(username: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            try {
                if (validateInput(username, password, confirmPassword)) {
                    val registerData = RegisterData(username, password, confirmPassword)
                    usersRepository.addUser(registerData)
                    _navigateToSuggestions.value = true
                }
            } catch (exception: Exception) {
                exception.message?.let { Log.e("error", it) }
            }
        }
    }

    private fun validateInput(username: String, password: String, confirmPassword: String): Boolean {
        if (username == "" || password == "" || confirmPassword == "") {
            _isFormFilledIn.value = false
            return false
        }
        if (password != confirmPassword) {
            _arePasswordsEqual.value = false
            return false
        }
        return true
    }
}