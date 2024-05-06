package com.rpsouza.bancodigital.presenter.features.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.domain.deposit.GetDepositUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositReceiptViewModel @Inject constructor(
  private val getDepositUseCase: GetDepositUseCase,
) : ViewModel() {

  fun getDeposit(idDeposit: String) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val result = getDepositUseCase.invoke(idDeposit)

      emit(StateView.Success(result))

    } catch (e: Exception) {
      emit(StateView.Error(e.message))
    }
  }
}