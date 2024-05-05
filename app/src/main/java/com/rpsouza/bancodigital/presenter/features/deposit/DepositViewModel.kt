package com.rpsouza.bancodigital.presenter.features.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.data.model.Deposit
import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.domain.deposit.SaveDepositUseCase
import com.rpsouza.bancodigital.domain.transaction.SaveTransactionUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
  private val saveDepositUseCase: SaveDepositUseCase,
  private val saveTransactionUseCase: SaveTransactionUseCase
) : ViewModel() {

  fun saveDeposit(deposit: Deposit) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val result = saveDepositUseCase.invoke(deposit)

      emit(StateView.Success(result))

    } catch (e: Exception) {
      emit(StateView.Error(e.message))
    }
  }

  fun saveTransaction(transaction: Transaction) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      saveTransactionUseCase.invoke(transaction)

      emit(StateView.Success(null))

    } catch (e: Exception) {
      emit(StateView.Error(e.message))
    }
  }
}