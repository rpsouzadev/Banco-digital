package com.rpsouza.bancodigital.domain.transaction

import com.rpsouza.bancodigital.data.enum.TransactionType
import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.data.repository.transaction.TransactionDataSourceImpl
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
  private val transactionDataSourceImpl: TransactionDataSourceImpl
) {

  suspend operator fun invoke(): Float {
    var cashIn = 0f
    var cashOut = 0f

   val transactions = transactionDataSourceImpl.getTransaction()

    transactions.forEach { transaction ->
      if (transaction.type == TransactionType.CASH_IN) {
        cashIn += transaction.amount
      } else {
        cashOut += transaction.amount
      }
    }

    val wallet = cashIn - cashOut
    return wallet
  }
}