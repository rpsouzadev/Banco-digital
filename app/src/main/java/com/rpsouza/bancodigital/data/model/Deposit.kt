package com.rpsouza.bancodigital.data.model

import com.google.firebase.database.FirebaseDatabase

data class Deposit(
  var id: String = "",
  var data: Long = 0,
  var amount: Float = 0f,
) {
  init {
    this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
  }
}
