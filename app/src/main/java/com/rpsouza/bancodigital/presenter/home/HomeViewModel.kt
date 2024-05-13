package com.rpsouza.bancodigital.presenter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.domain.profile.GetProfileUseCase
import com.rpsouza.bancodigital.domain.transaction.GetTransactionsUseCase
import com.rpsouza.bancodigital.domain.wallet.GetWalletUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getWalletUseCase: GetWalletUseCase,
  private val getProfileUseCase: GetProfileUseCase,
  private val getTransactionsUseCase: GetTransactionsUseCase
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

  fun getTransactions() = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val transactions = getTransactionsUseCase.invoke()

      emit(StateView.Success(transactions))
    } catch (ex: Exception) {
      emit(StateView.Error(ex.message))
    }
  }

  fun getProfile() = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val profile = getProfileUseCase.invoke()

      emit(StateView.Success(profile))
    } catch (ex: Exception) {
      emit(StateView.Error(ex.message))
    }
  }
}