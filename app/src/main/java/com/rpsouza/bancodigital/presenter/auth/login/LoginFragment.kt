package com.rpsouza.bancodigital.presenter.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.databinding.FragmentLoginBinding
import com.rpsouza.bancodigital.utils.BaseFragment
import com.rpsouza.bancodigital.utils.FirebaseHelper
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
  private var _binding: FragmentLoginBinding? = null
  private val binding get() = _binding!!

  private val loginViewModel: LoginViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentLoginBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initListeners()
  }

  private fun initListeners() {
    binding.buttonLogin.setOnClickListener { validateData() }

    binding.buttonRegisterLink.setOnClickListener {
      findNavController().navigate(
        R.id.action_loginFragment_to_registerFragment
      )
    }

    binding.buttonRecoverLink.setOnClickListener {
      findNavController().navigate(
        R.id.action_loginFragment_to_recoverFragment
      )
    }

  }

  private fun validateData() {
    val email = binding.editEmail.text.toString().trim()
    val password = binding.editPassword.text.toString().trim()

    if (email.isNotEmpty()) {
      if (password.isNotEmpty()) {
        loginUser(email, password)
        hideKeyboard()
      } else {
        showBottomSheet(message = getString(R.string.text_password_empty))
      }
    } else {
      showBottomSheet(message = getString(R.string.text_email_empty))
    }
  }

  private fun loginUser(email: String, password: String) {

    loginViewModel.login(email, password).observe(viewLifecycleOwner) { stateView ->

      when (stateView) {
        is StateView.Loading -> {
          binding.progressBarLogin.isVisible = true
        }

        is StateView.Success -> {
          binding.progressBarLogin.isVisible = false

          val navOption: NavOptions = NavOptions.Builder().setPopUpTo(R.id.nav_auth, true).build()
          findNavController().navigate(R.id.action_global_homeFragment, null, navOption)
        }

        is StateView.Error -> {
          binding.progressBarLogin.isVisible = false
          val message = FirebaseHelper.validError(stateView.message.toString())

          showBottomSheet(message = getString(message))
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}