package com.rpsouza.bancodigital.presenter.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.domain.auth.RegisterUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
  ViewModel() {

  fun register(
    name: String,
    phone: String,
    email: String,
    password: String
  ) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val user = registerUseCase.invoke(name, phone, email, password)

      emit(StateView.Success(user))

    } catch (e: Exception) {
      emit(StateView.Error(e.message))
    }
  }
}