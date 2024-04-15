package com.rpsouza.bancodigital.presenter.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.databinding.FragmentLoginBinding
import com.rpsouza.bancodigital.databinding.FragmentSplashBinding
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
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
    binding.buttomLogin.setOnClickListener { validateData() }

    binding.buttomRegisterLink.setOnClickListener {
      findNavController().navigate(
        R.id.action_loginFragment_to_registerFragment
      )
    }

    binding.buttomRecoverLink.setOnClickListener {
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
      } else {
        Toast.makeText(
          requireContext(),
          "Preencha a senha",
          Toast.LENGTH_SHORT
        ).show()
      }
    } else {
      Toast.makeText(
        requireContext(),
        "Preencha o email",
        Toast.LENGTH_SHORT
      ).show()
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

         findNavController().navigate(R.id.action_global_homeFragment)
        }

        is StateView.Error -> {
          binding.progressBarLogin.isVisible = false

          Toast.makeText(
            requireContext(),
            stateView.message,
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}