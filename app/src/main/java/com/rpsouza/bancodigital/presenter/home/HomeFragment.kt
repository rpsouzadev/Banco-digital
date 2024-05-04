package com.rpsouza.bancodigital.presenter.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.model.Wallet
import com.rpsouza.bancodigital.databinding.FragmentHomeBinding
import com.rpsouza.bancodigital.utils.GetMask
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!

  private val homeViewModel: HomeViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    getWallet()
    initListeners()
  }

  private fun initListeners() {
    binding.cardDeposit.setOnClickListener {
      findNavController().navigate(R.id.action_homeFragment_to_depositFormFragment)
    }
  }

  private fun getWallet() {
    homeViewModel.getWallet().observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {

        }

        is StateView.Success -> {
          stateView.data?.let {
            showBalance(it)
          }
        }

        is StateView.Error -> {
          showBottomSheet(message = stateView.message)
        }
      }
    }
  }

  private fun showBalance(wallet: Wallet) {
    binding.textBalance.text =
      getString(R.string.text_formated_value, GetMask.getFormatedValue(wallet.balance))
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}