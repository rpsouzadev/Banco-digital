package com.rpsouza.bancodigital.presenter.auth.register

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
import com.rpsouza.bancodigital.data.model.Wallet
import com.rpsouza.bancodigital.databinding.FragmentRegisterBinding
import com.rpsouza.bancodigital.presenter.profile.ProfileViewModel
import com.rpsouza.bancodigital.presenter.wallet.WalletViewModel
import com.rpsouza.bancodigital.utils.FirebaseHelper
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.initToolbar
import com.rpsouza.bancodigital.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

  private var _binding: FragmentRegisterBinding? = null
  private val binding get() = _binding!!

  private val registerViewModel: RegisterViewModel by viewModels()
  private val profileViewModel: ProfileViewModel by viewModels()
  private val walletViewModel: WalletViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentRegisterBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initToolbar(binding.toolbar)
    initListeners()
  }

  private fun initListeners() {
    binding.buttonRegister.setOnClickListener { validateData() }

  }

  private fun validateData() {
    val name = binding.editNameRegister.text.toString().trim()
    val email = binding.editEmailRegister.text.toString().trim()
    val phone = binding.editPhoneRegister.unMaskedText
    val password = binding.editPasswordRegister.text.toString().trim()
    val passwordConfirm = binding.editPasswordConfirmRegister.text.toString().trim()

    if (
      name.isNotEmpty() &&
      email.isNotEmpty() &&
      phone?.isNotEmpty() == true &&
      password.isNotEmpty() &&
      passwordConfirm.isNotEmpty()
    ) {

      if (phone.length == 11) {
        if (password == passwordConfirm) {
          registerUser(name, phone, email, password)
        } else {
          showBottomSheet(message = getString(R.string.text_bottom_sheet_confirm_password))
        }
      } else {
        showBottomSheet(message = getString(R.string.text_phone_invalid))
      }

    } else {
      showBottomSheet(message = getString(R.string.text_fields_empty))
    }
  }

  private fun registerUser(name: String, phone: String, email: String, password: String) {

    registerViewModel.register(name, phone, email, password)
      .observe(viewLifecycleOwner) { stateView ->

        when (stateView) {
          is StateView.Loading -> {
            binding.progressBar.isVisible = true
          }

          is StateView.Success -> {
            stateView.data?.let { saveProfile(it) }
          }

          is StateView.Error -> {
            binding.progressBar.isVisible = false
            val message = FirebaseHelper.validError(stateView.message.toString())

            showBottomSheet(message = getString(message))
          }
        }
      }
  }

  private fun saveProfile(user: User) {

    profileViewModel.saveProfile(user).observe(viewLifecycleOwner) { stateView ->

      when (stateView) {
        is StateView.Loading -> {

        }

        is StateView.Success -> {
          iniWallet()
        }

        is StateView.Error -> {
          binding.progressBar.isVisible = false
          val message = FirebaseHelper.validError(stateView.message.toString())

          showBottomSheet(message = getString(message))
        }
      }
    }
  }

  private fun iniWallet() {

    walletViewModel.initWallet(
      Wallet(
        userId = FirebaseHelper.getUserId()
      )
    ).observe(viewLifecycleOwner) { stateView ->

      when (stateView) {
        is StateView.Loading -> {

        }

        is StateView.Success -> {
          binding.progressBar.isVisible = false

          findNavController().navigate(R.id.action_global_homeFragment)
        }

        is StateView.Error -> {
          binding.progressBar.isVisible = false
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