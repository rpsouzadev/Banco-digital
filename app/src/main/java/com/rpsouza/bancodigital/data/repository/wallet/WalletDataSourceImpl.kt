package com.rpsouza.bancodigital.data.repository.wallet

import com.google.firebase.database.FirebaseDatabase
import com.rpsouza.bancodigital.data.model.Wallet
import com.rpsouza.bancodigital.utils.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class WalletDataSourceImpl @Inject constructor(database: FirebaseDatabase) : IWalletDataSource {

  private val walletReference = database.reference
    .child("wallet")

  override suspend fun initWallet(wallet: Wallet) {
    return suspendCoroutine { continuation ->
      walletReference.child(wallet.id).setValue(wallet)
        .addOnCompleteListener { task ->
          if (task.isSuccessful) {
            continuation.resumeWith(Result.success(Unit))
          } else {
            task.exception?.let {
              continuation.resumeWith(Result.failure(it))
            }
          }
        }
    }
  }
}