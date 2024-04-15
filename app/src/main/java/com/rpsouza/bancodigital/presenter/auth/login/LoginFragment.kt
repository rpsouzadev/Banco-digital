package com.rpsouza.bancodigital.presenter.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.databinding.FragmentLoginBinding
import com.rpsouza.bancodigital.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
  private var _binding: FragmentLoginBinding? = null
  private val binding get() = _binding!!

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

        Toast.makeText(
          requireContext(),
          "Login...",
          Toast.LENGTH_SHORT
        ).show()

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

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}