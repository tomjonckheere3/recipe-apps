package be.howest.tomjonckheere3.recipeapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import be.howest.tomjonckheere3.recipeapp.R
import be.howest.tomjonckheere3.recipeapp.databinding.FragmentRegisterBinding
import be.howest.tomjonckheere3.recipeapp.viewmodels.RegisterViewModel
import java.lang.Exception

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(inflater,
                R.layout.fragment_register,container,false)

        binding.registerButton.setOnClickListener {
            try {
                val username = binding.registerUsername.text.toString()
                val password = binding.registerPassword.text.toString()
                val confirmPassword = binding.registerConfirm.text.toString()
                viewModel.registerUser(username, password, confirmPassword)
            } catch (e: Exception) {
                e.message?.let { it1 -> Log.e("error", it1) }
            }

        }

        val errorTextView = binding.registerError

        viewModel.arePasswordsEqual.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it == false) {
                errorTextView.text = "Passwords are not equal"
                errorTextView.visibility = View.VISIBLE
            }
        })

        viewModel.isFormFilledIn.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it == false) {
                binding.registerError.text = "Please fill in all fields"
                errorTextView.visibility = View.VISIBLE
            }
        })

        viewModel.navigateToSuggestions.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it == true) {
                try {

                    val intent = Intent(activity, MainActivity::class.java)
                    activity?.startActivity(intent)
                } catch (exc: Exception) {
                    exc.message?.let { it1 -> Log.e("error", it1) }
                }
            }
        })

        return binding.root
    }

}