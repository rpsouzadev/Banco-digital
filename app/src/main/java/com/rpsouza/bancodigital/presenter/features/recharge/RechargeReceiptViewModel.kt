package com.rpsouza.bancodigital.presenter.features.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.domain.recharge.GetRechargeUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RechargeReceiptViewModel @Inject constructor(
  private val getRechargeUseCase: GetRechargeUseCase,
) : ViewModel() {

  fun getRecharge(idRecharge: String) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val result = getRechargeUseCase.invoke(idRecharge)

      emit(StateView.Success(result))

    } catch (e: Exception) {
      emit(StateView.Error(e.message))
    }
  }
}