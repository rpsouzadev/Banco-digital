package com.rpsouza.bancodigital.presenter.features.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.model.Deposit
import com.rpsouza.bancodigital.databinding.FragmentDepositReceiptBinding
import com.rpsouza.bancodigital.utils.GetMask
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositReceiptFragment : Fragment() {
  private var _binding: FragmentDepositReceiptBinding? = null
  private val binding get() = _binding!!

  private val args: DepositReceiptFragmentArgs by navArgs()
  private val depositReceiptViewModel: DepositReceiptViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentDepositReceiptBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    getDeposit()
    initListeners()
  }

  private fun initListeners() {
    binding.btnContinue.setOnClickListener { findNavController().popBackStack() }
  }

  private fun getDeposit() {
    depositReceiptViewModel.getDeposit(args.idDeposit).observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {

        }

        is StateView.Success -> {
          stateView.data?.let { configData(it) }
        }

        is StateView.Error -> {
          Toast.makeText(requireContext(), R.string.error_generic, Toast.LENGTH_SHORT).show()
          findNavController().popBackStack()
        }
      }
    }
  }

  private fun configData(deposit: Deposit) {
    with(binding) {
      textCodeTransaction.text = deposit.id
      textDate.text = GetMask.getFormatedDate(deposit.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
      textAmount.text =
        getString(R.string.text_formated_value, GetMask.getFormatedValue(deposit.amount))
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}