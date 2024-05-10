package com.rpsouza.bancodigital.presenter.features.transfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.databinding.FragmentTransferUserListBinding
import com.rpsouza.bancodigital.utils.initToolbar

class TransferUserListFragment : Fragment() {
  private var _binding: FragmentTransferUserListBinding? = null
  private val binding get() = _binding!!

  private lateinit var adapterTransferUser: TransferUserAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
     _binding = FragmentTransferUserListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initToolbar(binding.toolbar, light = true)
    initRecyclerView()
  }

  private fun initRecyclerView() {
    adapterTransferUser = TransferUserAdapter { userSelected ->
      Toast.makeText(requireContext(), userSelected.name, Toast.LENGTH_SHORT).show()
    }
    
    with(binding.rvUsers) {
      setHasFixedSize(true)
      adapter = adapterTransferUser
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}