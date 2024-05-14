package com.rpsouza.bancodigital.data.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
  var id: String = "",
  var name: String = "",
  val email: String = "",
  var phone: String = "",
  var image: String = "",

  @get:Exclude
  val password: String = ""
) : Parcelable
