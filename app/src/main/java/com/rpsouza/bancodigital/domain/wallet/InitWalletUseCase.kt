package com.rpsouza.bancodigital.domain.wallet

import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.data.model.Wallet
import com.rpsouza.bancodigital.data.repository.wallet.IWalletDataSource
import com.rpsouza.bancodigital.data.repository.wallet.WalletDataSourceImpl
import javax.inject.Inject

class InitWalletUseCase @Inject constructor(
  private val walletDataSource: WalletDataSourceImpl
) {

  suspend operator fun invoke(wallet: Wallet) {
    return walletDataSource.initWallet(wallet)
  }

}