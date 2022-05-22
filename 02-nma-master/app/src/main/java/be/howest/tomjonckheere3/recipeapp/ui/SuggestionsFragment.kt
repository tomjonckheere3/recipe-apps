package be.howest.tomjonckheere3.recipeapp.ui

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import be.howest.tomjonckheere3.recipeapp.R
import be.howest.tomjonckheere3.recipeapp.databinding.FragmentSuggestionsBinding
import be.howest.tomjonckheere3.recipeapp.receiver.AlarmReceiver
import be.howest.tomjonckheere3.recipeapp.viewmodels.SuggestionsViewModel
import java.util.*

class SuggestionsFragment : Fragment() {

    private val viewModel: SuggestionsViewModel by lazy {
        ViewModelProvider(this).get(SuggestionsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSuggestionsBinding>(inflater,
            R.layout.fragment_suggestions, container, false)

        viewModel.suggestionsRefreshed.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it == true) {
                val breakfast = viewModel.suggestions.value?.breakfast
                val supper = viewModel.suggestions.value?.supper
                val dinner = viewModel.suggestions.value?.dinner
                binding.breakfast = breakfast
                binding.supper = supper
                binding.dinner = dinner
                binding.breakfastCard.setOnClickListener {
                    viewModel.displayRecipeDetails(breakfast)
                }
                binding.supperCard.setOnClickListener {
                    viewModel.displayRecipeDetails(supper)
                }
                binding.dinnerCard.setOnClickListener {
                    viewModel.displayRecipeDetails(dinner)
                }
            }
        })

        viewModel.navigateToSelectedRecipe.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                        SuggestionsFragmentDirections.actionSuggestionsFragmentToDetailedRecipeFragment(it))
                viewModel.displayRecipeDetailsComplete()
            }
        })

        createChannel(
            getString(R.string.recipe_notification_channel_id),
            getString(R.string.recipe_notification_channel_name)
        )



        return binding.root
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.recipe_notification_channel_description)

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }


}