package com.rpsouza.bancodigital.presenter.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.domain.profile.GetProfileUseCase
import com.rpsouza.bancodigital.domain.profile.SaveImageProfileUseCase
import com.rpsouza.bancodigital.domain.profile.SaveProfileUseCase
import com.rpsouza.bancodigital.utils.FirebaseHelper
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
  private val saveProfileUseCase: SaveProfileUseCase,
  private val saveImageProfileUseCase: SaveImageProfileUseCase,
  private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

  fun saveProfile(user: User) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      saveProfileUseCase.invoke(user)

      emit(StateView.Success(null))

    } catch (e: Exception) {
      emit(StateView.Error(e.message))
    }
  }

  fun saveImageProfile(imageProfile: String) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val urlImage = saveImageProfileUseCase.invoke(imageProfile)

      emit(StateView.Success(urlImage))

    } catch (e: Exception) {
      emit(StateView.Error(e.message))
    }
  }

  fun getProfile() = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val user = getProfileUseCase.invoke(FirebaseHelper.getUserId())

      emit(StateView.Success(user))

    } catch (e: Exception) {
      emit(StateView.Error(e.message))
    }
  }
}