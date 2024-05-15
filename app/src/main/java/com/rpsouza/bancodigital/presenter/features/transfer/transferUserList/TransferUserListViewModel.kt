package com.rpsouza.bancodigital.presenter.features.transfer.transferUserList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rpsouza.bancodigital.domain.profile.GetProfileListUseCase
import com.rpsouza.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TransferUserListViewModel @Inject constructor(
  private val getProfileListUseCase: GetProfileListUseCase
) : ViewModel() {

  fun getProfileList() = liveData(Dispatchers.IO) {
    try {
      emit(StateView.Loading())

      val profileList = getProfileListUseCase.invoke()

      emit(StateView.Success(profileList))
    } catch (ex: Exception) {
      emit(StateView.Error(ex.message))
    }
  }
}