package com.rpsouza.bancodigital.domain.profile

import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.data.repository.profile.ProfileDataSourceImpl
import javax.inject.Inject

class SaveImageProfileUseCase @Inject constructor(
  private val profileRepositoryImpl: ProfileDataSourceImpl
) {

  suspend operator fun invoke(imageProfile: String): String {
    return profileRepositoryImpl.saveImage(imageProfile)
  }

}