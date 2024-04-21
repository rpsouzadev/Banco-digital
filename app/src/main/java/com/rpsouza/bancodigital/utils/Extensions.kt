package com.rpsouza.bancodigital.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.databinding.LayoutBottomSheetBinding

fun Fragment.initToolbar(toolbar: Toolbar, homeAsUpEnabled: Boolean = true) {
  (activity as AppCompatActivity).setSupportActionBar(toolbar)

  (activity as AppCompatActivity).title = ""

  (activity as AppCompatActivity)
    .supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)

  (activity as AppCompatActivity)
    .supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)

  toolbar.setNavigationOnClickListener {
    activity?.onBackPressedDispatcher?.onBackPressed()
  }
}

fun Fragment.showBottomSheet(
  titleDialog: Int? = null,
  titleButton: Int? = null,
  message: String?,
  onClick: () -> Unit = {}
) {

  val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
  val bottomSheetBinding: LayoutBottomSheetBinding =
    LayoutBottomSheetBinding.inflate(layoutInflater, null, false)

  bottomSheetBinding.textTitleBottomSheet.text =
    getString(titleDialog ?: R.string.text_title_bottom_sheet)

  bottomSheetBinding.textMessageBottomSheet.text = message ?: getText(R.string.error_generic)

  bottomSheetBinding.buttonBottomSheet.text =
    getString(titleButton ?: R.string.text_button_bottom_sheet)

  bottomSheetBinding.buttonBottomSheet.setOnClickListener {
    onClick()
    bottomSheetDialog.dismiss()
  }

  bottomSheetDialog.setContentView(bottomSheetBinding.root)
  bottomSheetDialog.show()
}