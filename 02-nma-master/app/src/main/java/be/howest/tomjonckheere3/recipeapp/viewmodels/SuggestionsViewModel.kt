package be.howest.tomjonckheere3.recipeapp.viewmodels

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import be.howest.tomjonckheere3.recipeapp.R
import be.howest.tomjonckheere3.recipeapp.database.getDatabase
import be.howest.tomjonckheere3.recipeapp.domain.NormalRecipe
import be.howest.tomjonckheere3.recipeapp.receiver.AlarmReceiver
import be.howest.tomjonckheere3.recipeapp.repository.RecipesRepository
import be.howest.tomjonckheere3.recipeapp.util.sendNotification
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class SuggestionsViewModel(application: Application): AndroidViewModel(application) {

    private val recipesRepository = RecipesRepository(getDatabase(application))

    val suggestions = recipesRepository.suggestedNormalRecipes

    private var _suggestionsRefreshed = MutableLiveData<Boolean>(false)

    val suggestionsRefreshed: LiveData<Boolean>
        get() = _suggestionsRefreshed

    private val _navigateToSelectedRecipe = MutableLiveData<Int>()

    val navigateToSelectedRecipe: LiveData<Int>
        get() = _navigateToSelectedRecipe

    private val notifyPendingIntent: PendingIntent
    private val alarmManager = getApplication<Application>().getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notifyIntent = Intent(getApplication(), AlarmReceiver::class.java)

    private val HOUR_TO_SHOW_PUSH = 16

    init {
        getSuggestions()
        notifyPendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        schedulePushNotifications()
    }

    private fun getSuggestions() {
        viewModelScope.launch {
            try {
                recipesRepository.refreshRecipes()
                recipesRepository.refreshSuggestions()
            } catch (exception: Exception) {
                exception.message?.let { Timber.e(it) }
            }
            _suggestionsRefreshed.value = true
        }
    }

    fun displayRecipeDetails(recipe: NormalRecipe?) {
        _navigateToSelectedRecipe.value = recipe?.recipeId
    }
    fun displayRecipeDetailsComplete() {
        _navigateToSelectedRecipe.value = null
    }

    private fun schedulePushNotifications() {
        val calendar = GregorianCalendar.getInstance().apply {
            if (get(Calendar.HOUR_OF_DAY) >= HOUR_TO_SHOW_PUSH) {
                add(Calendar.DAY_OF_MONTH, 1)
            }

            set(Calendar.HOUR_OF_DAY, HOUR_TO_SHOW_PUSH)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        Timber.d(calendar.toString())
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            notifyPendingIntent
        )
    }
}