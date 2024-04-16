package com.rpsouza.bancodigital.data.repository.auth

import com.google.firebase.auth.FirebaseUser
import com.rpsouza.bancodigital.data.model.User

interface IAuthFirebaseDataSource {
  suspend fun login(email: String, password: String)

  suspend fun register(
    name: String,
    phone: String,
    email: String,
    password: String
  ): User


  suspend fun recover(email: String)
}