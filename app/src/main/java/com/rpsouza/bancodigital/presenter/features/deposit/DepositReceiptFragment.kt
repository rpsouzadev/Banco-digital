package com.rpsouza.bancodigital.presenter.features.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.databinding.FragmentDepositReceiptBinding
import com.rpsouza.bancodigital.utils.GetMask

class DepositReceiptFragment : Fragment() {
  private var _binding: FragmentDepositReceiptBinding? = null
  private val binding get() = _binding!!

  private val args: DepositReceiptFragmentArgs by navArgs()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentDepositReceiptBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initListeners()
    configData()
  }

  private fun initListeners() {
    binding.btnContinue.setOnClickListener { findNavController().popBackStack() }
  }

  private fun configData() {
    with(binding) {
      textCodeTransaction.text = args.deposit.id
      textDate.text = GetMask.getFormatedDate(args.deposit.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
      textAmount.text =
        getString(R.string.text_formated_value, GetMask.getFormatedValue(args.deposit.amount))
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}