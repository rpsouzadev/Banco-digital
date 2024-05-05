package com.rpsouza.bancodigital.data.repository.transaction

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.utils.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransactionDataSourceImpl @Inject constructor(
  database: FirebaseDatabase
) : ITransactionDataSource {

  private val transactionReference = database.reference
    .child("transaction")
    .child(FirebaseHelper.getUserId())

  override suspend fun saveTransaction(transaction: Transaction) {
    return suspendCoroutine { continuation ->
      transactionReference.child(transaction.id)
        .setValue(transaction).addOnCompleteListener { task ->
          if (task.isSuccessful) {
            val dateReference = transactionReference
              .child(transaction.id)
              .child("date")

            dateReference.setValue(ServerValue.TIMESTAMP)
              .addOnCompleteListener { taskUpdate ->
                if (taskUpdate.isSuccessful) {
                  continuation.resumeWith(Result.success(Unit))
                } else {
                  taskUpdate.exception?.let {
                    continuation.resumeWith(Result.failure(it))
                  }
                }
              }
          } else {
            task.exception?.let {
              continuation.resumeWith(Result.failure(it))
            }
          }
        }
    }
  }

}