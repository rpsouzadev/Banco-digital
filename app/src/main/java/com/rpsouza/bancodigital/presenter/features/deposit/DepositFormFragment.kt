package com.rpsouza.bancodigital.presenter.features.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.rpsouza.bancodigital.NavMainDirections
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.enum.TransactionOperation
import com.rpsouza.bancodigital.data.enum.TransactionType
import com.rpsouza.bancodigital.data.model.Deposit
import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.databinding.FragmentDepositFormBinding
import com.rpsouza.bancodigital.utils.BaseFragment
import com.rpsouza.bancodigital.utils.MoneyTextWatcher
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.initToolbar
import com.rpsouza.bancodigital.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositFormFragment : BaseFragment() {
  private var _binding: FragmentDepositFormBinding? = null
  private val binding get() = _binding!!

  private val depositViewModel: DepositViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentDepositFormBinding.inflate(inflater, container, false)
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

    binding.btnContinue.setOnClickListener { validateDeposit() }
  }

  private fun validateDeposit() {
    val amount = MoneyTextWatcher.getValueUnMasked(binding.editAmount)

    if (amount > 0f) {
      val deposit = Deposit(amount = amount)
      hideKeyboard()
      saveDeposit(deposit)
    } else {
      Toast.makeText(requireContext(), "Digite um valor maior que 0,00", Toast.LENGTH_SHORT).show()
    }
  }

  private fun saveDeposit(deposit: Deposit) {
    depositViewModel.saveDeposit(deposit).observe(viewLifecycleOwner) { stateView ->
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

  private fun saveTransaction(deposit: Deposit) {
    val transaction = Transaction(
      id = deposit.id,
      date = deposit.date,
      amount = deposit.amount,
      type = TransactionType.CASH_IN,
      operation = TransactionOperation.DEPOSIT,
    )


    depositViewModel.saveTransaction(transaction).observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {
        }

        is StateView.Success -> {
          val action = NavMainDirections
            .actionGlobalDepositReceiptFragment(deposit.id, false)

          val navOption: NavOptions = NavOptions.Builder().setPopUpTo(R.id.depositFormFragment , true).build()
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