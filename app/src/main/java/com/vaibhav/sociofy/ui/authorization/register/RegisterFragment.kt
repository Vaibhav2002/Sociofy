package com.vaibhav.sociofy.ui.authorization.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        binding.goToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.registerButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val password = binding.passInput.text.toString()
            registerViewModel.registerUser(username, email, password)
        }
        lifecycleScope.launchWhenStarted {
            registerViewModel.registerState.collect { state ->
                when (state) {
                    is RegisterViewModel.Companion.Events.Success -> {
                        binding.loadingAnim.isVisible = false
                        showToast(state.successMessage)
                        findNavController().navigate(R.id.action_registerFragment_to_profileImageFragment)
                    }
                    is RegisterViewModel.Companion.Events.Failure -> {
                        binding.loadingAnim.isVisible = false
                        Timber.d(state.failureMessage)
                        showToast(state.failureMessage)
                    }
                    RegisterViewModel.Companion.Events.Loading -> binding.loadingAnim.isVisible =
                        true
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}