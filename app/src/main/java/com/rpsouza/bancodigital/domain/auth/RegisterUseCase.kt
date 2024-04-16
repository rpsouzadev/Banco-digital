package com.rpsouza.bancodigital.domain.auth

import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
  private val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
) {
  suspend operator fun invoke(
    name: String,
    phone: String,
    email: String,
    password: String
  ): User {
    return authFirebaseDataSourceImpl.register(name, phone, email, password)
  }
}