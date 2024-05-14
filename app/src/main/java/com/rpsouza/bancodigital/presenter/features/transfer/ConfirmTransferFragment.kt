package com.rpsouza.bancodigital.presenter.features.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.databinding.FragmentConfirmTransferBinding
import com.rpsouza.bancodigital.utils.GetMask
import com.rpsouza.bancodigital.utils.initToolbar
import com.squareup.picasso.Picasso

class ConfirmTransferFragment : Fragment() {
  private var _binding: FragmentConfirmTransferBinding? = null
  private val binding get() = _binding!!

  private val args: ConfirmTransferFragmentArgs by navArgs()

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
  }


  private fun configData() {
    Picasso.get()
      .load(args.user.image)
      .fit().centerCrop()
      .into(binding.userImage)

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