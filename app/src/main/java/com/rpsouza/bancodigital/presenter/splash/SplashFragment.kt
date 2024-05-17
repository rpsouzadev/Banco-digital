package com.rpsouza.bancodigital.presenter.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.databinding.FragmentSplashBinding
import com.rpsouza.bancodigital.utils.FirebaseHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
  private var _binding: FragmentSplashBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSplashBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    Handler(Looper.getMainLooper()).postDelayed(this::verifyAuth, 3000)
  }

  private fun verifyAuth() {
    val action = if (FirebaseHelper.isAuthenticated()) {
      R.id.action_global_homeFragment
    } else {
      R.id.action_global_nav_auth
    }

    val navOption: NavOptions = NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
    findNavController().navigate(action, null, navOption)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}