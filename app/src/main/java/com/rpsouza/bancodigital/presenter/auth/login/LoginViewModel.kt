package com.rpsouza.bancodigital.presenter.auth.login

import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.domain.auth.LoginUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) {

  fun login(email: String, password: String) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      loginUseCase.invoke(email, password)

      emit(StateView.Success(null))

    } catch (e: Exception) {
      emit(StateView.Error(e.message))
    }
  }
}