package com.rpsouza.bancodigital.data.repository.wallet

import com.rpsouza.bancodigital.data.model.Wallet

interface IWalletDataSource {

  suspend fun initWallet(wallet: Wallet)

  suspend fun getWallet(): Wallet
}