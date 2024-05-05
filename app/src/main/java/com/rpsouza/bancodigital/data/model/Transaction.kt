package com.rpsouza.bancodigital.data.model

import com.rpsouza.bancodigital.data.enum.TransactionOperation
import com.rpsouza.bancodigital.data.enum.TransactionType

data class Transaction(
  var id: String = "",
  val date: Long = 0,
  val amount: Float = 0f,
  var type: TransactionType? = null,
  val operation: TransactionOperation? = null,
)
