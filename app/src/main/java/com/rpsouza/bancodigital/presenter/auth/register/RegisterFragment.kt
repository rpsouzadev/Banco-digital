package com.rpsouza.bancodigital.presenter.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rpsouza.bancodigital.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
  private var _binding: FragmentRegisterBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentRegisterBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initListeners()
  }

  private fun initListeners() {
    binding.buttomRegister.setOnClickListener { validateData() }

  }

  private fun validateData() {
    val name = binding.editNameRegister.text.toString().trim()
    val email = binding.editEmailRegister.text.toString().trim()
    val phone = binding.editPhoneRegister.text.toString().trim()
    val password = binding.editPasswordRegister.text.toString().trim()
    val passwordConfirm = binding.editPasswordConfirmRegister.text.toString().trim()

    if (
      name.isNotEmpty() &&
      email.isNotEmpty() &&
      phone.isNotEmpty() &&
      password.isNotEmpty() &&
      passwordConfirm.isNotEmpty()
    ) {

      if (password == passwordConfirm) {
        Toast.makeText(
          requireContext(),
          "Register...",
          Toast.LENGTH_SHORT
        ).show()
      } else {
        Toast.makeText(
          requireContext(),
          "Senha não confere...",
          Toast.LENGTH_SHORT
        ).show()
      }

    } else {
      Toast.makeText(
        requireContext(),
        "Preencha todos os campos",
        Toast.LENGTH_SHORT
      ).show()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}