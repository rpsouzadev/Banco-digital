package com.rpsouza.bancodigital.data.model

import com.google.firebase.database.FirebaseDatabase

data class Extract(
  var id: String = "",
  var data: Long = 0,
  var type: String = "",
  var amount: Float = 0f,
  var operation: String = ""
) {
  init {
    this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
  }
}
