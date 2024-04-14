package com.rpsouza.bancodigital.presenter.auth.recover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rpsouza.bancodigital.databinding.FragmentRecoverBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoverFragment : Fragment() {
  private var _binding: FragmentRecoverBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentRecoverBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initListeners()
  }

  private fun initListeners() {
    binding.buttomRecover.setOnClickListener { validateData() }


  }

  private fun validateData() {
    val email = binding.editEmailRecover.text.toString().trim()

    if (email.isNotEmpty()) {
      Toast.makeText(
        requireContext(),
        "Recuperando...",
        Toast.LENGTH_SHORT
      ).show()
    } else {
      Toast.makeText(
        requireContext(),
        "Preencha o email",
        Toast.LENGTH_SHORT
      ).show()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}