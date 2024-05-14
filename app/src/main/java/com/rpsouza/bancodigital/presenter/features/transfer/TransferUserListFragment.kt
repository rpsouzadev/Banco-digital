package com.rpsouza.bancodigital.presenter.features.transfer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferfalk.simplesearchview.SimpleSearchView
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

  private var profileList: List<User> = listOf()

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
    configSearchView()
  }

  private fun configSearchView() {
    binding.searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String): Boolean {
        return false
      }

      override fun onQueryTextChange(newText: String): Boolean {
        return if (newText.isNotEmpty()) {
          val newList = profileList.filter { user ->
            user.name.contains(newText, true)
          }
          adapterTransferUser.submitList(newList)
          true
        } else {
          adapterTransferUser.submitList(profileList)
          false
        }
      }

      override fun onQueryTextCleared(): Boolean {
        return false
      }
    })

    binding.searchView.setOnSearchViewListener(object : SimpleSearchView.SearchViewListener {
      override fun onSearchViewShown() {
      }

      override fun onSearchViewClosed() {
        adapterTransferUser.submitList(profileList)
      }

      override fun onSearchViewShownAnimation() {
      }

      override fun onSearchViewClosedAnimation() {
      }
    })
  }

  private fun getProfileList() {
    transferUserListViewModel.getProfileList().observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {
          binding.progressBar.isVisible = true
        }

        is StateView.Success -> {
          binding.progressBar.isVisible = false
          profileList = stateView.data ?: emptyList()
          adapterTransferUser.submitList(profileList)
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
      val action = TransferUserListFragmentDirections
        .actionTransferUserListFragmentToTransferFormFragment(userSelected)
      findNavController().navigate(action)
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