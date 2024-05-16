package com.rpsouza.bancodigital.presenter.features.transfer.transferReceipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.domain.profile.GetProfileUseCase
import com.rpsouza.bancodigital.domain.transfer.GetTransferUserCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TransferReceiptViewModel @Inject constructor(
 private val getTransferUserCase: GetTransferUserCase,
 private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

  fun getTransfer(idTransfer: String) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val transfer = getTransferUserCase.invoke(idTransfer)

      emit(StateView.Success(transfer))
    } catch (ex: Exception) {
      emit(StateView.Error(ex.message))
    }
  }

  fun getProfile(idUser: String) = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val user = getProfileUseCase.invoke(idUser)

      emit(StateView.Success(user))

    } catch (e: Exception) {
      emit(StateView.Error(e.message))
    }
  }
}