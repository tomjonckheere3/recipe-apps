package be.howest.tomjonckheere3.recipeapp.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import be.howest.tomjonckheere3.recipeapp.R
import be.howest.tomjonckheere3.recipeapp.receiver.AlarmReceiver
import be.howest.tomjonckheere3.recipeapp.ui.MainActivity
import retrofit2.http.Body

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.recipe_notification_channel_id)
    )
        .setSmallIcon(R.drawable.servings_icon)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)

    notify(NOTIFICATION_ID, builder.build())
}

