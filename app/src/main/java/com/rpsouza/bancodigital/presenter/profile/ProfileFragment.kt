package com.rpsouza.bancodigital.presenter.profile

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.databinding.FragmentProfileBinding
import com.rpsouza.bancodigital.utils.BaseFragment
import com.rpsouza.bancodigital.utils.FirebaseHelper
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.initToolbar
import com.rpsouza.bancodigital.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
  private var _binding: FragmentProfileBinding? = null
  private val binding get() = _binding!!

  private val profileViewModel: ProfileViewModel by viewModels()
  private var user: User? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    _binding = FragmentProfileBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initToolbar(binding.toolbar)
    initListeners()
    getProfile()
  }

  private fun initListeners() {
    binding.buttonSaveProfile.setOnClickListener {
      if (user != null) validateData()
    }
  }

  private fun checkPermissionCamera() {
    val permissionListener: PermissionListener = object : PermissionListener() {
      override fun onPermissionGranted() {
        Toast.makeText(requireContext(), "Permissão aceita", Toast.LENGTH_SHORT).show()
      }

      override fun onPermissionDenied(deniedPermissions: List<String?>) {
        Toast.makeText(requireContext(), "Permissão negada", Toast.LENGTH_SHORT).show()
      }
    }

    showDialogPermissionDenied(
      permissionListener = permissionListener,
      permission = Manifest.permission.CAMERA,
      message = R.string.text_message_camera_denied_profile_fragment
    )
  }

  private fun checkPermissionGallery() {
    val permissionListener: PermissionListener = object : PermissionListener() {
      override fun onPermissionGranted() {
        Toast.makeText(requireContext(), "Permissão aceita", Toast.LENGTH_SHORT).show()
      }

      override fun onPermissionDenied(deniedPermissions: List<String?>) {
        Toast.makeText(requireContext(), "Permissão negada", Toast.LENGTH_SHORT).show()
      }
    }

    showDialogPermissionDenied(
      permissionListener = permissionListener,
      permission = Manifest.permission.READ_EXTERNAL_STORAGE,
      message = R.string.text_message_gallery_denied_profile_fragment
    )
  }

  private fun showDialogPermissionDenied(
    permissionListener: PermissionListener,
    permission: String,
    message: Int,
  ) {
    TedPermission.create()
      .setPermissionListener(permissionListener)
      .setDeniedTitle("Permissão negada")
      .setDeniedMessage(message)
      .setDeniedCloseButtonText("Não")
      .setGotoSettingButtonText("Sim")
      .setPermissions(permission)
      .check()
  }

  private fun getProfile() {
    profileViewModel.getProfile().observe(viewLifecycleOwner) { stateView ->
      when (stateView) {
        is StateView.Loading -> {
          binding.progressBar.isVisible = true
        }

        is StateView.Success -> {
          binding.progressBar.isVisible = false
          stateView.data?.let { user = it }
          configData()
        }

        is StateView.Error -> {
          binding.progressBar.isVisible = false
          val message = FirebaseHelper.validError(stateView.message.toString())
          showBottomSheet(message = getString(message))
        }
      }
    }
  }

  private fun saveProfile() {
    user?.let {
      profileViewModel.saveProfile(it).observe(viewLifecycleOwner) { stateView ->
        when (stateView) {
          is StateView.Loading -> {
            binding.progressBar.isVisible = true
          }

          is StateView.Success -> {
            binding.progressBar.isVisible = false
            Toast.makeText(
              requireContext(),
              R.string.text_message_save_success_profile_fragment,
              Toast.LENGTH_SHORT
            ).show()
          }

          is StateView.Error -> {
            binding.progressBar.isVisible = false
            val message = FirebaseHelper.validError(stateView.message.toString())
            showBottomSheet(message = getString(message))
          }
        }
      }
    }
  }

  private fun validateData() {
    val name = binding.editNameProfile.text.toString().trim()
    val phone = binding.editPhoneProfile.unMaskedText

    if (
      name.isNotEmpty() &&
      phone?.isNotEmpty() == true
    ) {

      if (phone.length == 11) {
        user?.let {
          it.name = name
          it.phone = phone
        }

        hideKeyboard()
        saveProfile()
      } else {
        showBottomSheet(message = getString(R.string.text_phone_invalid))
      }

    } else {
      showBottomSheet(message = getString(R.string.text_fields_empty))
    }
  }

  private fun configData() {
    binding.editNameProfile.setText(user?.name)
    binding.editEmailProfile.setText(user?.email)
    binding.editPhoneProfile.setText(user?.phone)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}