package com.rpsouza.bancodigital.presenter.features.transfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.databinding.FragmentTransferUserListBinding
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.initToolbar
import com.rpsouza.bancodigital.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferUserListFragment : Fragment() {
  private var _binding: FragmentTransferUserListBinding? = null
  private val binding get() = _binding!!

  private lateinit var adapterTransferUser: TransferUserAdapter
  private val transferUserListViewModel: TransferUserListViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentTransferUserListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initToolbar(binding.toolbar, light = true)
    initRecyclerView()
    getProfileList()
  }

  private fun getProfileList() {
    transferUserListViewModel.getProfileList().observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {
          binding.progressBar.isVisible = true
        }

        is StateView.Success -> {
          binding.progressBar.isVisible = false

          stateView.data?.let { adapterTransferUser.submitList(it) }
        }

        is StateView.Error -> {
          binding.progressBar.isVisible = false
          showBottomSheet(message = stateView.message)
        }
      }
    }
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

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_search, menu)
    val item = menu.findItem(R.id.action_search)
    binding.searchView.setMenuItem(item)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}