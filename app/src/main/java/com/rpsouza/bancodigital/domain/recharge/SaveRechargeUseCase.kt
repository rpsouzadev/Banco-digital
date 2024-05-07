package com.rpsouza.bancodigital.domain.recharge

import com.rpsouza.bancodigital.data.model.Recharge
import com.rpsouza.bancodigital.data.repository.recharge.RechargeDataSourceImpl
import javax.inject.Inject

class SaveRechargeUseCase @Inject constructor(
  private val rechargeDataSourceImpl: RechargeDataSourceImpl
) {
  suspend operator fun invoke(recharge: Recharge): Recharge {
    return rechargeDataSourceImpl.saveRecharge(recharge)
  }
}