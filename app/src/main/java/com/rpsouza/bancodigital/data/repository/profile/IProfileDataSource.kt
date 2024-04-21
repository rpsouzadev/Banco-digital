package com.rpsouza.bancodigital.data.repository.profile

import com.rpsouza.bancodigital.data.model.User

interface IProfileDataSource {

  suspend fun saveProfile(user: User)
}