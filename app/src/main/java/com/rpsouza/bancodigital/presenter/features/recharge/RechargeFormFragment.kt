package com.rpsouza.bancodigital.presenter.features.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.rpsouza.bancodigital.NavMainDirections
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.enum.TransactionOperation
import com.rpsouza.bancodigital.data.enum.TransactionType
import com.rpsouza.bancodigital.data.model.Recharge
import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.databinding.FragmentRechargeFormBinding
import com.rpsouza.bancodigital.utils.BaseFragment
import com.rpsouza.bancodigital.utils.MoneyTextWatcher
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.initToolbar
import com.rpsouza.bancodigital.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeFormFragment : BaseFragment() {
  private var _binding: FragmentRechargeFormBinding? = null
  private val binding get() = _binding!!

  private val rechargeViewModel: RechargeViewModel by viewModels()

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
    with(binding.editAmount) {
      addTextChangedListener(MoneyTextWatcher(this))

      addTextChangedListener {
        if (MoneyTextWatcher.getValueUnMasked(this) > 300f) {
          this.setText("R$ 0,00")
        }
      }

      doAfterTextChanged {
        this.text.length.let {
          this.setSelection(it)
        }
      }
    }

    binding.btnConfirm.setOnClickListener { validateData() }
  }

  private fun validateData() {
    val amount = MoneyTextWatcher.getValueUnMasked(binding.editAmount)
    val phone = binding.editPhoneNumber.unMaskedText

    if (amount >= 10f) {
      if (phone?.length == 11) {
        val recharge = Recharge(amount = amount.toFloat(), phoneNumber = phone)
        hideKeyboard()
        saveRecharge(recharge)
      } else {
        showBottomSheet(message = getString(R.string.text_phone_invalid))
      }
    } else {
      showBottomSheet(message = getString(R.string.text_message_invalid_value_recharge_form_fragment))
    }
  }

  private fun saveRecharge(recharge: Recharge) {
    rechargeViewModel.saveRecharge(recharge).observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {
          binding.progressBar.isVisible = true
        }

        is StateView.Success -> {
          stateView.data?.let { saveTransaction(it) }
        }

        is StateView.Error -> {
          binding.progressBar.isVisible = false
          showBottomSheet(message = stateView.message)
        }
      }
    }
  }

  private fun saveTransaction(recharge: Recharge) {
    val transaction = Transaction(
      id = recharge.id,
      date = recharge.date,
      amount = recharge.amount,
      type = TransactionType.CASH_OUT,
      operation = TransactionOperation.RECHARGE,
    )


    rechargeViewModel.saveTransaction(transaction).observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {
        }

        is StateView.Success -> {
          val action = NavMainDirections
            .actionGlobalRechargeReceiptFragment(recharge.id)

          val navOption: NavOptions = NavOptions.Builder().setPopUpTo(R.id.rechargeFormFragment, true).build()
          findNavController().navigate(action, navOption)
        }

        is StateView.Error -> {
          binding.progressBar.isVisible = false
          showBottomSheet(message = stateView.message)
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}