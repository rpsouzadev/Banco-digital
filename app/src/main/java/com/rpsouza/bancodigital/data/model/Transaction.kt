package com.rpsouza.bancodigital.data.model

import com.rpsouza.bancodigital.data.enum.TransactionOperation
import com.rpsouza.bancodigital.data.enum.TransactionType

data class Transaction(
  var id: String = "",
  var date: Long = System.currentTimeMillis(),
  val amount: Float = 0f,
  var type: TransactionType? = null,
  val operation: TransactionOperation? = null,
)
