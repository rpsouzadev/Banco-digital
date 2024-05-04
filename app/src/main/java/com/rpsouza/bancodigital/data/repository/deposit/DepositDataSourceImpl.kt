package com.rpsouza.bancodigital.data.repository.deposit

import com.google.firebase.database.FirebaseDatabase
import com.rpsouza.bancodigital.data.model.Deposit
import com.rpsouza.bancodigital.utils.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class DepositDataSourceImpl @Inject constructor(
  database: FirebaseDatabase
) : IDepositDataSource {

  private val depositReference = database.reference
    .child("deposit")
    .child(FirebaseHelper.getUserId())

  override suspend fun saveDeposit(deposit: Deposit): String {
    return suspendCoroutine { continuation ->
      depositReference.child(deposit.id)
        .setValue(deposit).addOnCompleteListener { task ->
          if (task.isSuccessful) {
            continuation.resumeWith(Result.success(deposit.id))
          } else {
            task.exception?.let {
              continuation.resumeWith(Result.failure(it))
            }
          }
        }
    }
  }

}