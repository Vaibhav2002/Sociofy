package com.vaibhav.sociofy.ui.authorization.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.databinding.FragmentLoginBinding
import com.vaibhav.sociofy.ui.HomeScreen.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        binding.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passInput.text.toString()
            loginViewModel.loginUser(email, password)
        }
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginState.collect { state ->
                when (state) {
                    is LoginViewModel.Companion.Events.Success -> {
                        binding.loadingAnim.isVisible = false
                        showToast(state.successMessage)
                        requireActivity().startActivity(
                            Intent(
                                requireContext(),
                                HomeActivity::class.java
                            )
                        )
                        requireActivity().finish()
                    }
                    is LoginViewModel.Companion.Events.Failure -> {
                        binding.loadingAnim.isVisible = false
                        Timber.d(state.failureMessage)
                        showToast(state.failureMessage)
                    }
                    LoginViewModel.Companion.Events.Loading -> binding.loadingAnim.isVisible = true
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (loginViewModel.isLoggedIn()) {
            requireActivity().startActivity(Intent(requireContext(), HomeActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}