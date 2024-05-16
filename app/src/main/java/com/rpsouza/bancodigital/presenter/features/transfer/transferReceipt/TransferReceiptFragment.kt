package com.rpsouza.bancodigital.presenter.features.transfer.transferReceipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.databinding.FragmentTransferReceiptBinding
import com.rpsouza.bancodigital.utils.GetMask
import com.rpsouza.bancodigital.utils.initToolbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferReceiptFragment : Fragment() {
  private var _binding: FragmentTransferReceiptBinding? = null
  private val binding get() = _binding!!

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
    initToolbar(binding.toolbar, homeAsUpEnabled = false)
    initListeners()
  }

  private fun initListeners() {
    binding.btnConfirm.setOnClickListener { findNavController().popBackStack() }
  }

  private fun configData(user: User) {
    if (user.image.isNotEmpty()) {
      Picasso.get()
        .load(user.image)
        .fit().centerCrop()
        .into(binding.userImage)
    } else {
      binding.userImage.setImageResource(R.drawable.img_profile_default)
    }

    binding.textUsername.text = user.name
//    binding.textAmount.text = getString(
//      R.string.text_formated_value,
//      GetMask.getFormatedValue(args.amount)
//    )
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}