package com.rpsouza.bancodigital.domain.recharge

import com.rpsouza.bancodigital.data.model.Recharge
import com.rpsouza.bancodigital.data.repository.recharge.RechargeDataSourceImpl
import javax.inject.Inject

class GetRechargeUseCase @Inject constructor(
  private val rechargeDataSourceImpl: RechargeDataSourceImpl
) {
  suspend operator fun invoke(idRecharge: String): Recharge {
    return rechargeDataSourceImpl.getRecharge(idRecharge)
  }
}