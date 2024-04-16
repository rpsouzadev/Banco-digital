package com.rpsouza.bancodigital.domain.profile

import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.data.repository.profile.ProfileRepositoryImpl
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
  private val profileRepositoryImpl: ProfileRepositoryImpl
) {

  suspend operator fun invoke(user: User) {
    return profileRepositoryImpl.saveProfile(user)
  }

}