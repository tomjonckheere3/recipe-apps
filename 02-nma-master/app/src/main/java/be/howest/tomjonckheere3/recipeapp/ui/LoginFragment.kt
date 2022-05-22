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
import androidx.navigation.findNavController
import be.howest.tomjonckheere3.recipeapp.R
import be.howest.tomjonckheere3.recipeapp.databinding.FragmentLoginBinding
import be.howest.tomjonckheere3.recipeapp.viewmodels.LoginViewModel
import java.lang.Exception

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater,
            R.layout.fragment_login,container,false)

        binding.registerButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_login_fragment_to_register_fragment)
        }

        binding.loginButton.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()
            viewModel.loginUser(username, password)
        }

        val errorTextView = binding.loginError

        viewModel.isFormFilledIn.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it == false) {
                errorTextView.text = "Please fill in all fields"
                errorTextView.visibility = View.VISIBLE
            }
        })

        viewModel.doesUserExist.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it == false) {
                errorTextView.text = "This user does not exist"
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