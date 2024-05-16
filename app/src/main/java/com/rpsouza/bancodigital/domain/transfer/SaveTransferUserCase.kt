package com.rpsouza.bancodigital.domain.transfer

import com.rpsouza.bancodigital.data.model.Transfer
import com.rpsouza.bancodigital.data.repository.transfer.TransferDataSourceImpl
import javax.inject.Inject

class SaveTransferUserCase @Inject constructor(
  private val transferDataSourceImpl: TransferDataSourceImpl
) {

  suspend operator fun invoke(transfer: Transfer) {
    transferDataSourceImpl.saveTransfer(transfer)
  }
}