package com.rpsouza.bancodigital.data.model

import com.google.firebase.database.FirebaseDatabase

data class Recharge(
  var id: String = "",
  var date: Long = 0L,
  var amount: Float = 0f,
  var phoneNumber: String = "",
) {
  init {
    this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
  }
}