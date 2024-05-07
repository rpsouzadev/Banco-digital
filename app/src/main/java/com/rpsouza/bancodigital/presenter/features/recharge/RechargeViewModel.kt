package com.rpsouza.bancodigital.presenter.features.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.data.model.Recharge
import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.domain.recharge.SaveRechargeUseCase
import com.rpsouza.bancodigital.domain.transaction.SaveTransactionUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(
  private val saveRechargeUseCase: SaveRechargeUseCase,
  private val saveTransactionUseCase: SaveTransactionUseCase
) : ViewModel() {

  fun saveRecharge(recharge: Recharge) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val result = saveRechargeUseCase.invoke(recharge)

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