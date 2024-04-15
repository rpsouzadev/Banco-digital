package com.rpsouza.bancodigital.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.rpsouza.bancodigital.R

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