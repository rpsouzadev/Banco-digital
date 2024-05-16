package com.rpsouza.bancodigital.data.model

import android.os.Parcelable
import com.google.firebase.database.FirebaseDatabase
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transfer(
  var id: String = "",
  var date: Long = 0,
  var amount: Float = 0f,
  val idUserSent: String = "",
  var idUserReceived: String = "",
) : Parcelable {
  init {
    this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
  }
}
