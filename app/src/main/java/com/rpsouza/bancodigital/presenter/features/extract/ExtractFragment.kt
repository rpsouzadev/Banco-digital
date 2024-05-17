package com.rpsouza.bancodigital.presenter.features.extract

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rpsouza.bancodigital.data.enum.TransactionOperation
import com.rpsouza.bancodigital.databinding.FragmentExtractBinding
import com.rpsouza.bancodigital.presenter.home.TransactionAdapter
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.initToolbar
import com.rpsouza.bancodigital.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExtractFragment : Fragment() {
  private var _binding: FragmentExtractBinding? = null
  private val binding get() = _binding!!

  private val extractViewModel: ExtractViewModel by viewModels()
  private lateinit var adapterTransaction: TransactionAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentExtractBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initToolbar(binding.toolbar)
    configRecyclerView()
    getTransactions()
  }

  private fun configRecyclerView() {
    adapterTransaction = TransactionAdapter(requireContext()) { transaction ->
      when (transaction.operation) {
        TransactionOperation.DEPOSIT -> {
          val action = ExtractFragmentDirections
            .actionExtractFragmentToDepositReceiptFragment(transaction.id, true)

          findNavController().navigate(action)
        }

        TransactionOperation.RECHARGE -> {
          val action = ExtractFragmentDirections
            .actionExtractFragmentToRechargeReceiptFragment(transaction.id)

          findNavController().navigate(action)
        }

        TransactionOperation.TRANSFER -> {
          val action = ExtractFragmentDirections
            .actionExtractFragmentToTransferReceiptFragment(transaction.id, homeAsUpEnabled = true)

          findNavController().navigate(action)
        }

        else -> {

        }
      }
    }
    with(binding.rvTransactions) {
      setHasFixedSize(true)
      adapter = adapterTransaction
    }
  }

  private fun getTransactions() {
    extractViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {
          binding.progressBar.isVisible = true
        }

        is StateView.Success -> {
          binding.progressBar.isVisible = false

          adapterTransaction.submitList(stateView.data?.reversed())
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