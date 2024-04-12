package com.rpsouza.bancodigital.domain.auth

import com.rpsouza.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl

class RecoverUseCase(private val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl) {
  suspend operator fun invoke(email: String) {
    return authFirebaseDataSourceImpl.recover(email)
  }
}