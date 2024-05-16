package com.rpsouza.bancodigital.presenter.features.transfer.transferReceipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.model.Transfer
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.databinding.FragmentTransferReceiptBinding
import com.rpsouza.bancodigital.utils.FirebaseHelper
import com.rpsouza.bancodigital.utils.GetMask
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.initToolbar
import com.rpsouza.bancodigital.utils.showBottomSheet
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferReceiptFragment : Fragment() {
  private var _binding: FragmentTransferReceiptBinding? = null
  private val binding get() = _binding!!

  private val args: TransferReceiptFragmentArgs by navArgs()
  private val transferReceiptViewModel: TransferReceiptViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentTransferReceiptBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initToolbar(binding.toolbar, homeAsUpEnabled = args.homeAsUpEnabled)
    getTransfer()
    initListeners()
  }

  private fun initListeners() {
    binding.btnConfirm.isVisible = !args.homeAsUpEnabled
    binding.btnConfirm.setOnClickListener { findNavController().popBackStack() }
  }

  private fun getTransfer() {
    transferReceiptViewModel.getTransfer(args.idTransfer).observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {}
        is StateView.Success -> {
          stateView.data?.let { transfer ->
            val userId = if (transfer.idUserSent == FirebaseHelper.getUserId()) {
              transfer.idUserReceived
            } else {
              transfer.idUserSent
            }

            getProfile(userId)
            configTransfer(transfer)
          }
        }

        is StateView.Error -> {
          Toast.makeText(requireContext(), R.string.error_generic, Toast.LENGTH_SHORT).show()
          findNavController().popBackStack()
        }
      }
    }
  }

  private fun getProfile(idUser: String) {
    transferReceiptViewModel.getProfile(idUser).observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {}
        is StateView.Success -> {
          stateView.data?.let { configProfile(it) }
        }

        is StateView.Error -> {
          Toast.makeText(requireContext(), R.string.error_generic, Toast.LENGTH_SHORT).show()
          findNavController().popBackStack()
        }
      }
    }
  }

  private fun configTransfer(transfer: Transfer) {
    binding.textCodeTransaction.text = transfer.id
    binding.textDate.text = GetMask.getFormatedDate(transfer.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
    binding.textAmount.text =
      getString(R.string.text_formated_value, GetMask.getFormatedValue(transfer.amount))
  }

  private fun configProfile(user: User) {
    if (user.image.isNotEmpty()) {
      Picasso.get()
        .load(user.image)
        .fit().centerCrop()
        .into(binding.userImage)
    } else {
      binding.userImage.setImageResource(R.drawable.img_profile_default)
    }

    binding.textUsername.text = user.name
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}