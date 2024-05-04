package com.rpsouza.bancodigital.domain.deposit

import com.rpsouza.bancodigital.data.model.Deposit
import com.rpsouza.bancodigital.data.repository.deposit.DepositDataSourceImpl
import javax.inject.Inject

class SaveDepositUseCase @Inject constructor(
  private val depositDataSourceImpl: DepositDataSourceImpl
) {

  suspend operator fun invoke(deposit: Deposit): Deposit {
    return depositDataSourceImpl.saveDeposit(deposit)
  }
}