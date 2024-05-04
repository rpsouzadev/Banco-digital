package com.rpsouza.bancodigital.presenter.features.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.data.model.Deposit
import com.rpsouza.bancodigital.domain.deposit.SaveDepositUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
  private val saveDepositUseCase: SaveDepositUseCase
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
}