package com.rpsouza.bancodigital.data.repository.auth

interface IAuthFirebaseDataSource {
  suspend fun login(email: String, password: String)

  suspend fun register(name: String, email: String, phone: String, password: String)

  suspend fun recover(email: String)
}