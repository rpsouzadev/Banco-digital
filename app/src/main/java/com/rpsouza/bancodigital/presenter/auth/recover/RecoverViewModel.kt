package com.rpsouza.bancodigital.presenter.auth.recover

import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.domain.auth.RecoverUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RecoverViewModel @Inject constructor(private val recoverUseCase: RecoverUseCase) {

  fun recover(email: String) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      recoverUseCase.invoke(email)

      emit(StateView.Success(null))

    } catch (e: Exception) {
      emit(StateView.Error(e.message))
    }
  }
}