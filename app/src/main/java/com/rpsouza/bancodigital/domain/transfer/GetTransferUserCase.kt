package com.rpsouza.bancodigital.domain.transfer

import com.rpsouza.bancodigital.data.model.Transfer
import com.rpsouza.bancodigital.data.repository.transfer.TransferDataSourceImpl
import javax.inject.Inject

class GetTransferUserCase @Inject constructor(
  private val transferDataSourceImpl: TransferDataSourceImpl
) {

  suspend operator fun invoke(idTransfer: String): Transfer {
    return transferDataSourceImpl.getTransfer(idTransfer)
  }
}