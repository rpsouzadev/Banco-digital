package com.rpsouza.bancodigital.data.repository.deposit

import com.rpsouza.bancodigital.data.model.Deposit

interface IDepositDataSource {

  suspend fun saveDeposit(deposit: Deposit): String
}