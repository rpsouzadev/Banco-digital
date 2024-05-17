package com.rpsouza.bancodigital.data.repository.transfer

import com.rpsouza.bancodigital.data.model.Transfer

interface ITransferDataSource {
  suspend fun getTransfer(idTransfer: String): Transfer

  suspend fun saveTransfer(transfer: Transfer)

  suspend fun saveTransferTransaction(transfer: Transfer)
}
