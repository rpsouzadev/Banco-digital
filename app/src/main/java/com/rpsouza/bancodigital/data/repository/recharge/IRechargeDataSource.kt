package com.rpsouza.bancodigital.data.repository.recharge

import com.rpsouza.bancodigital.data.model.Recharge

interface IRechargeDataSource {
  suspend fun saveRecharge(recharge: Recharge): Recharge

  suspend fun getRecharge(idRecharge: String): Recharge
}