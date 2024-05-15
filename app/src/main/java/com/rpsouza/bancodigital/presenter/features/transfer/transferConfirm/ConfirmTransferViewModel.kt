package com.rpsouza.bancodigital.presenter.features.transfer.transferConfirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.domain.transaction.GetBalanceUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ConfirmTransferViewModel @Inject constructor(
  private val getBalanceUseCase: GetBalanceUseCase
) : ViewModel() {

  fun getBalance() = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val balance = getBalanceUseCase.invoke()

      emit(StateView.Success(balance))
    } catch (ex: Exception) {
      emit(StateView.Error(ex.message))
    }
  }
}