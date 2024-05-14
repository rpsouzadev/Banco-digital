package com.rpsouza.bancodigital.presenter.features.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.databinding.FragmentTransferFormBinding
import com.rpsouza.bancodigital.utils.BaseFragment
import com.rpsouza.bancodigital.utils.MoneyTextWatcher
import com.rpsouza.bancodigital.utils.initToolbar
import com.rpsouza.bancodigital.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferFormFragment : BaseFragment() {
  private var _binding: FragmentTransferFormBinding? = null
  private val binding get() = _binding!!

  private val args: TransferFormFragmentArgs by navArgs()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentTransferFormBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initToolbar(toolbar = binding.toolbar, light = true)
    initListeners()
  }

  private fun initListeners() {
    with(binding.editAmount) {
      addTextChangedListener(MoneyTextWatcher(this))

      addTextChangedListener {
        if (MoneyTextWatcher.getValueUnMasked(this) > 99999.99f) {
          this.setText("R$ 0,00")
        }
      }

      doAfterTextChanged {
        this.text.length.let {
          this.setSelection(it)
        }
      }
    }

    binding.btnContinue.setOnClickListener { validateTransfer() }
  }

  private fun validateTransfer() {
    val amount = MoneyTextWatcher.getValueUnMasked(binding.editAmount)

    if (amount > 0f) {
      hideKeyboard()

      val action = TransferFormFragmentDirections
        .actionTransferFormFragmentToConfirmTransferFragment(user = args.user, amount = amount)

      findNavController().navigate(action)

    } else {
      showBottomSheet(message = getString(R.string.text_message_empty_amount_transfer_form_fragment))
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}