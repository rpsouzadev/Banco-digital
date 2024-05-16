package com.rpsouza.bancodigital.data.repository.transfer

import com.rpsouza.bancodigital.data.model.Transfer

interface ITransferDataSource {
  suspend fun saveTransfer(transfer: Transfer)
}