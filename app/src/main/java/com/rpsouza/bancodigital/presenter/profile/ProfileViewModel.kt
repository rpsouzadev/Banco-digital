package com.rpsouza.bancodigital.presenter.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.domain.profile.SaveProfileUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
  private val saveProfileUseCase: SaveProfileUseCase
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
}