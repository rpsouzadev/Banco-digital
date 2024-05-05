package com.rpsouza.bancodigital.data.repository.transaction

import com.rpsouza.bancodigital.data.model.Transaction

interface ITransactionDataSource {

  suspend fun saveTransaction(transaction: Transaction)
}