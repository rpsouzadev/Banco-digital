package com.rpsouza.bancodigital.presenter.features.deposit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.databinding.FragmentDepositFormBinding
import com.rpsouza.bancodigital.databinding.FragmentHomeBinding
import com.rpsouza.bancodigital.utils.initToolbar

class DepositFormFragment : Fragment() {
  private var _binding: FragmentDepositFormBinding? = null
  private val binding get() = _binding!!
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentDepositFormBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initToolbar(toolbar = binding.toolbar, light = true)
  }


  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}