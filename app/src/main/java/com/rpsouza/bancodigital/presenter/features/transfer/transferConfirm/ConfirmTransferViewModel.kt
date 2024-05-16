package com.rpsouza.bancodigital.presenter.features.transfer.transferConfirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.data.model.Transfer
import com.rpsouza.bancodigital.domain.transaction.GetBalanceUseCase
import com.rpsouza.bancodigital.domain.transfer.SaveTransferUserCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ConfirmTransferViewModel @Inject constructor(
  private val getBalanceUseCase: GetBalanceUseCase,
  private val saveTransferUseCase: SaveTransferUserCase
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

  fun saveTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      saveTransferUseCase.invoke(transfer)

      emit(StateView.Success(Unit))

    } catch (e: Exception) {
      emit(StateView.Error(e.message))
    }
  }
}