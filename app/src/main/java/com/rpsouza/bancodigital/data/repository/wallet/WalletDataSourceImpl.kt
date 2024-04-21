package com.rpsouza.bancodigital.data.repository.wallet

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rpsouza.bancodigital.data.model.Wallet
import com.rpsouza.bancodigital.utils.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class WalletDataSourceImpl @Inject constructor(database: FirebaseDatabase) : IWalletDataSource {

  private val userId = FirebaseHelper.getUserId()

  private val walletReference = database.reference
    .child("wallet")

  override suspend fun initWallet(wallet: Wallet) {

    return suspendCoroutine { continuation ->
      walletReference.child(userId).setValue(wallet)
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

  override suspend fun getWallet(): Wallet {
    return suspendCoroutine { continuation ->
      walletReference.child(userId)
        .addListenerForSingleValueEvent(object : ValueEventListener {
          override fun onDataChange(snapshot: DataSnapshot) {
            val wallet = snapshot.getValue(Wallet::class.java)

            wallet?.let { continuation.resumeWith(Result.success(it)) }
          }

          override fun onCancelled(error: DatabaseError) {
            continuation.resumeWith(Result.failure(error.toException()))
          }
        })
    }
  }
}