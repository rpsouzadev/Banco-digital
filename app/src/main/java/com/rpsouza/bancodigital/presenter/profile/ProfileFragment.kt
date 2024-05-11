package com.rpsouza.bancodigital.presenter.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.databinding.BottomSheetImageBinding
import com.rpsouza.bancodigital.databinding.FragmentProfileBinding
import com.rpsouza.bancodigital.databinding.LayoutBottomSheetBinding
import com.rpsouza.bancodigital.utils.BaseFragment
import com.rpsouza.bancodigital.utils.FirebaseHelper
import com.rpsouza.bancodigital.utils.StateView
import com.rpsouza.bancodigital.utils.initToolbar
import com.rpsouza.bancodigital.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
  private var _binding: FragmentProfileBinding? = null
  private val binding get() = _binding!!

  private val profileViewModel: ProfileViewModel by viewModels()
  private var user: User? = null

  private var imageProfile: String? = null
  private var currentPhotoPath: String? = null

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
    binding.imageUser.setOnClickListener { showBottomSheetImage() }

    binding.buttonSaveProfile.setOnClickListener {
      if (user != null) validateData()
    }
  }

  private fun showBottomSheetImage() {
    val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    val bottomSheetBinding: BottomSheetImageBinding =
      BottomSheetImageBinding.inflate(layoutInflater, null, false)

    bottomSheetBinding.buttonCamera.setOnClickListener {
      checkPermissionCamera()
      bottomSheetDialog.dismiss()
    }

    bottomSheetBinding.buttonGallery.setOnClickListener {
      checkPermissionGallery()
      bottomSheetDialog.dismiss()
    }

    bottomSheetDialog.setContentView(bottomSheetBinding.root)
    bottomSheetDialog.show()
  }

  private fun checkPermissionCamera() {
    val permissionListener: PermissionListener = object : PermissionListener {
      override fun onPermissionGranted() {
        openCamera()
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
    val permissionListener: PermissionListener = object : PermissionListener {
      override fun onPermissionGranted() {
        openGallery()
      }

      override fun onPermissionDenied(deniedPermissions: List<String?>) {
        Toast.makeText(requireContext(), "Permissão negada", Toast.LENGTH_SHORT).show()
      }
    }

    showDialogPermissionDenied(
      permissionListener = permissionListener,
      permission = Manifest.permission.READ_MEDIA_IMAGES,
      message = R.string.text_message_gallery_denied_profile_fragment
    )
  }

  private fun openCamera() {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    var photoFile: File? = null

    try {
      photoFile = createImageFile()
    } catch (ex: IOException) {
      Toast.makeText(
        requireContext(),
        "Não foi possível abrir a camera do dispositivo",
        Toast.LENGTH_SHORT
      ).show()
    }

    if (photoFile != null) {
      val photoURI = FileProvider.getUriForFile(
        requireContext(),
        "com.rpsouza.bancodigital.fileprovider",
        photoFile,
      )
      takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
      cameraLauncher.launch(takePictureIntent)
    }
  }

  private fun createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale("pt", "BR")).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val image = File.createTempFile(
      imageFileName,
      ".jpg",
      storageDir
    )

    currentPhotoPath = image.absolutePath
    return image
  }

  private val cameraLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) { activityResult ->
    if (activityResult.resultCode == Activity.RESULT_OK) {
      val file = File(currentPhotoPath!!)
      binding.imageUser.setImageURI(Uri.fromFile(file))

      imageProfile = file.toURI().toString()
    }
  }

  private fun openGallery() {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    galleryLauncher.launch(intent)
  }

  private val galleryLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) { activityResult ->
    if (activityResult.resultCode == Activity.RESULT_OK) {
      val imageSelected = activityResult.data!!.data
      imageProfile = imageSelected.toString()

      if (imageSelected != null) {
        binding.imageUser.setImageBitmap(getBitmap(imageSelected))
      }
    }
  }

  private fun getBitmap(pathUri: Uri): Bitmap? {
    var bitmap: Bitmap? = null

    try {
      bitmap = if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, pathUri)
      } else {
        val source = ImageDecoder.createSource(requireActivity().contentResolver, pathUri)
        ImageDecoder.decodeBitmap(source)
      }
    } catch (ex: Exception) {
      ex.printStackTrace()
    }

    return bitmap
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