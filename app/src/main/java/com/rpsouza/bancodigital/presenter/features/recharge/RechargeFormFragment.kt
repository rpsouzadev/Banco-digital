package com.rpsouza.bancodigital.presenter.features.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.databinding.FragmentRechargeFormBinding
import com.rpsouza.bancodigital.utils.initToolbar
import com.rpsouza.bancodigital.utils.showBottomSheet

class RechargeFormFragment : Fragment() {
  private var _binding: FragmentRechargeFormBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentRechargeFormBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initToolbar(binding.toolbar, light = true)
    initListeners()
  }

  private fun initListeners() {
    binding.btnConfirm.setOnClickListener { validateData() }
  }

  private fun validateData() {
    val amount = binding.editAmount.text.toString().trim()
    val phone = binding.editPhoneNumber.text.toString().trim()

    if (amount.isNotEmpty()) {
      if (phone.length == 11) {
        Toast.makeText(requireContext(), "Sucesso", Toast.LENGTH_SHORT).show()
      } else {
        showBottomSheet(message = getString(R.string.text_phone_invalid))
      }
    } else {
      showBottomSheet(message = getString(R.string.text_message_invalid_value_recharge_form_fragment))
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}