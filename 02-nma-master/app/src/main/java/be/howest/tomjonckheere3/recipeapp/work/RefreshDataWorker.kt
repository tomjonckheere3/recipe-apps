package be.howest.tomjonckheere3.recipeapp.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import be.howest.tomjonckheere3.recipeapp.R
import be.howest.tomjonckheere3.recipeapp.database.getDatabase
import be.howest.tomjonckheere3.recipeapp.repository.RecipesRepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "be.howest.tomjonckheere3.recipeapp.work.RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        Timber.d("do work")
        val database = getDatabase(applicationContext)
        val repository = RecipesRepository(database)

        try {
            repository.refreshRecipes()
        } catch (e: HttpException) {
            Timber.i(e)
            return Result.retry()
        }
        return Result.success()
    }

}