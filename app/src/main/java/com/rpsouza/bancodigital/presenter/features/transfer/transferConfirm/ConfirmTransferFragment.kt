package com.rpsouza.bancodigital.presenter.features.transfer.transferConfirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rpsouza.bancodigital.NavMainDirections
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.enum.TransactionOperation
import com.rpsouza.bancodigital.data.enum.TransactionType
import com.rpsouza.bancodigital.data.model.Deposit
import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.data.model.Transfer
import com.rpsouza.bancodigital.databinding.FragmentConfirmTransferBinding
import com.rpsouza.bancodigital.presenter.features.deposit.DepositFormFragmentDirections
import com.rpsouza.bancodigital.utils.FirebaseHelper
import com.rpsouza.bancodigital.utils.GetMask
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.initToolbar
import com.rpsouza.bancodigital.utils.showBottomSheet
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmTransferFragment : Fragment() {
  private var _binding: FragmentConfirmTransferBinding? = null
  private val binding get() = _binding!!

  private val args: ConfirmTransferFragmentArgs by navArgs()
  private val confirmTransferViewModel: ConfirmTransferViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentConfirmTransferBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initToolbar(binding.toolbar)
    configData()
    initListeners()
  }

  private fun initListeners() {
    binding.btnConfirm.setOnClickListener {
      binding.btnConfirm.isEnabled = false
      getBalance()
    }
  }

  private fun getBalance() {
    confirmTransferViewModel.getBalance().observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {
          binding.progressBar.isVisible = true
        }

        is StateView.Success -> {
          if ((stateView.data ?: 0f) >= args.amount) {
            val transfer = Transfer(
              idUserReceived = args.user.id,
              idUserSent = FirebaseHelper.getUserId(),
              amount = args.amount
            )

            saveTransfer(transfer)
          } else {
            binding.btnConfirm.isEnabled = true
            showBottomSheet(message = getString(R.string.text_message_insufficient_balance_confirm_transfer_fragment))
          }
        }

        is StateView.Error -> {
          binding.progressBar.isVisible = false
          binding.btnConfirm.isEnabled = true
          val message = FirebaseHelper.validError(stateView.message.toString())
          showBottomSheet(message = getString(message))
        }
      }
    }
  }

  private fun saveTransfer(transfer: Transfer) {
    confirmTransferViewModel.saveTransfer(transfer).observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {
        }

        is StateView.Success -> {
          saveTransaction(transfer)
        }

        is StateView.Error -> {
          binding.progressBar.isVisible = false
          val message = FirebaseHelper.validError(stateView.message.toString())
          showBottomSheet(message = getString(message))
        }
      }
    }
  }

  private fun saveTransaction(transfer: Transfer) {
    confirmTransferViewModel.saveTransaction(transfer).observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {
        }

        is StateView.Success -> {
          val action = NavMainDirections
            .actionGlobalTransferReceiptFragment(idTransfer = transfer.id, homeAsUpEnabled = false)
          val navOption: NavOptions = NavOptions.Builder().setPopUpTo(R.id.transferFormFragment, true).build()
          findNavController().navigate(action, navOption)
        }

        is StateView.Error -> {
          binding.progressBar.isVisible = false
          showBottomSheet(message = stateView.message)
        }
      }
    }
  }

  private fun configData() {
    if (args.user.image.isNotEmpty()) {
      Picasso.get()
        .load(args.user.image)
        .fit().centerCrop()
        .into(binding.userImage)
    } else {
      binding.userImage.setImageResource(R.drawable.img_profile_default)
    }

    binding.textUsername.text = args.user.name
    binding.textAmount.text = getString(
      R.string.text_formated_value,
      GetMask.getFormatedValue(args.amount)
    )
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}