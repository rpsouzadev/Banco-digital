package com.rpsouza.bancodigital.domain.deposit

import com.rpsouza.bancodigital.data.model.Deposit
import com.rpsouza.bancodigital.data.repository.deposit.DepositDataSourceImpl
import javax.inject.Inject

class GetDepositUseCase @Inject constructor(
  private val depositDataSourceImpl: DepositDataSourceImpl
) {

  suspend operator fun invoke(idDeposit: String): Deposit {
    return depositDataSourceImpl.getDeposit(idDeposit)
  }
}