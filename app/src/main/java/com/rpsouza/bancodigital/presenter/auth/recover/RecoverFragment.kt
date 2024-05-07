package com.rpsouza.bancodigital.presenter.auth.recover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.databinding.FragmentRecoverBinding
import com.rpsouza.bancodigital.utils.BaseFragment
import com.rpsouza.bancodigital.utils.FirebaseHelper
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.initToolbar
import com.rpsouza.bancodigital.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoverFragment : BaseFragment() {
  private var _binding: FragmentRecoverBinding? = null
  private val binding get() = _binding!!

  private val recoverViewModel: RecoverViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentRecoverBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initToolbar(binding.toolbar)
    initListeners()
  }

  private fun initListeners() {
    binding.buttonRecover.setOnClickListener { validateData() }
  }

  private fun validateData() {
    val email = binding.editEmailRecover.text.toString().trim()

    if (email.isNotEmpty()) {
      recoverUser(email)
      hideKeyboard()
    } else {
      showBottomSheet(message = getString(R.string.text_email_empty))
    }
  }

  private fun recoverUser(email: String) {

    recoverViewModel.recover(email).observe(viewLifecycleOwner) { stateView ->

      when (stateView) {
        is StateView.Loading -> {
          binding.progressBar.isVisible = true
        }

        is StateView.Success -> {
          binding.progressBar.isVisible = false

          showBottomSheet(
            message = getString(R.string.text_message_send_email_sucess_recover_fragment)
          )
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