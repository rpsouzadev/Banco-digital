package com.rpsouza.bancodigital.presenter.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.enum.TransactionOperation
import com.rpsouza.bancodigital.data.enum.TransactionType
import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.databinding.FragmentHomeBinding
import com.rpsouza.bancodigital.utils.FirebaseHelper
import com.rpsouza.bancodigital.utils.GetMask
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!

  private val homeViewModel: HomeViewModel by viewModels()
  private lateinit var adapterTransaction: TransactionAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    configRecyclerView()
    getTransactions()
    initListeners()
  }

  private fun initListeners() {
    binding.btnLogout.setOnClickListener {
      FirebaseHelper.getAuth().signOut()
      findNavController().navigate(R.id.action_homeFragment_to_nav_auth)
    }

    binding.cardDeposit.setOnClickListener {
      findNavController().navigate(R.id.action_homeFragment_to_depositFormFragment)
    }

    binding.cardExtract.setOnClickListener {
      findNavController().navigate(R.id.action_homeFragment_to_extractFragment)
    }

    binding.btnShowAll.setOnClickListener {
      findNavController().navigate(R.id.action_homeFragment_to_extractFragment)
    }

    binding.cardProfile.setOnClickListener {
      findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
    }
  }

  private fun configRecyclerView() {
    adapterTransaction = TransactionAdapter(requireContext()) { transaction ->
      when (transaction.operation) {
        TransactionOperation.DEPOSIT -> {
          val action = HomeFragmentDirections
            .actionHomeFragmentToDepositReceiptFragment(transaction.id, true)

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
    homeViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {
          binding.progressBar.isVisible = true
        }

        is StateView.Success -> {
          binding.progressBar.isVisible = false

          adapterTransaction.submitList(stateView.data?.reversed()?.take(6))

          showBalance(stateView.data ?: emptyList())
        }

        is StateView.Error -> {
          binding.progressBar.isVisible = false
          showBottomSheet(message = stateView.message)
        }
      }
    }
  }

  private fun showBalance(transactions: List<Transaction>) {
    var cashIn = 0f
    var cashOut = 0f

    transactions.forEach { transaction ->
      if (transaction.type == TransactionType.CASH_IN) {
        cashIn += transaction.amount
      } else {
        cashOut += transaction.amount
      }
    }

    val wallet = cashIn - cashOut

    binding.textBalance.text =
      getString(R.string.text_formated_value, GetMask.getFormatedValue(wallet))
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}