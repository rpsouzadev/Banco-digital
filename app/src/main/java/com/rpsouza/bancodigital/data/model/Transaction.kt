package com.rpsouza.bancodigital.data.model

import com.google.firebase.database.FirebaseDatabase

data class Transaction(
  var id: String = "",
  val description: String = "",
  val data: Long = 0,
  val value: Float = 0f,
) {
  init {
    this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
  }
}
