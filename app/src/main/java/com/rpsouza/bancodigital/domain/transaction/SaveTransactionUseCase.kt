package com.rpsouza.bancodigital.domain.transaction

import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.data.repository.transaction.TransactionDataSourceImpl
import javax.inject.Inject

class SaveTransactionUseCase @Inject constructor(
  private val transactionDataSourceImpl: TransactionDataSourceImpl
) {

  suspend operator fun invoke(transaction: Transaction) {
    transactionDataSourceImpl.saveTransaction(transaction)
  }
}