package com.rpsouza.bancodigital.domain.transaction

import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.data.repository.transaction.TransactionDataSourceImpl
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
  private val transactionDataSourceImpl: TransactionDataSourceImpl
) {

  suspend operator fun invoke(): List<Transaction> {
    return transactionDataSourceImpl.getTransaction()
  }
}