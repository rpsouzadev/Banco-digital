package com.rpsouza.bancodigital.presenter.features.recharge

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
import com.rpsouza.bancodigital.data.model.Recharge
import com.rpsouza.bancodigital.databinding.FragmentRechargeReceiptBinding
import com.rpsouza.bancodigital.utils.Constants
import com.rpsouza.bancodigital.utils.GetMask
import com.rpsouza.bancodigital.utils.Mask
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.initToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeReceiptFragment : Fragment() {
  private var _binding: FragmentRechargeReceiptBinding? = null
  private val binding get() = _binding!!

  private val args: RechargeReceiptFragmentArgs by navArgs()
  private val rechargeReceiptViewModel: RechargeReceiptViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentRechargeReceiptBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initToolbar(toolbar = binding.toolbar)
    getRecharge()
  }

  private fun getRecharge() {
    rechargeReceiptViewModel.getRecharge(args.idRecharge).observe(viewLifecycleOwner) { stateView ->
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

  private fun configData(recharge: Recharge) {
    with(binding) {
      textCodeTransaction.text = recharge.id
      textDate.text = GetMask.getFormatedDate(recharge.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
      textAmount.text =
        getString(R.string.text_formated_value, GetMask.getFormatedValue(recharge.amount))
      textPhoneNumber.text = Mask.mask(Constants.Mask.MASK_PHONE, recharge.phoneNumber)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}