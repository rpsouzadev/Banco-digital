package com.rpsouza.bancodigital.domain.profile

import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.data.repository.profile.ProfileDataSourceImpl
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
  private val profileRepositoryImpl: ProfileDataSourceImpl
) {

  suspend operator fun invoke(): User {
    return profileRepositoryImpl.getProfile()
  }
}