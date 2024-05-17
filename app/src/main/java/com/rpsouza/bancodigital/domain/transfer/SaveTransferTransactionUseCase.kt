package com.rpsouza.bancodigital.domain.transfer

import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.data.model.Transfer
import com.rpsouza.bancodigital.data.repository.transfer.TransferDataSourceImpl
import javax.inject.Inject

class SaveTransferTransactionUseCase @Inject constructor(
  private val transferDataSourceImpl: TransferDataSourceImpl
) {

  suspend operator fun invoke(transfer: Transfer) {
    transferDataSourceImpl.saveTransferTransaction(transfer)
  }
}