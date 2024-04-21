package com.rpsouza.bancodigital.presenter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.domain.wallet.GetWalletUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getWalletUseCase: GetWalletUseCase
) : ViewModel() {

  fun getWallet() = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val wallet = getWalletUseCase.invoke()

      emit(StateView.Success(wallet))
    } catch (ex: Exception) {
      emit(StateView.Error(ex.message))
    }
  }
}