package com.rpsouza.bancodigital.domain.auth

import com.rpsouza.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl

class RegisterUseCase(private val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl) {
  suspend operator fun invoke(name: String, email: String, phone: String, password: String) {
    return authFirebaseDataSourceImpl.register(name, email, phone, password)
  }
}