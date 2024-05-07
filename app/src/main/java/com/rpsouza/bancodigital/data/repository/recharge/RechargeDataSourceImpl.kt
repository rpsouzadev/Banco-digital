package com.rpsouza.bancodigital.data.repository.recharge

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.rpsouza.bancodigital.data.model.Deposit
import com.rpsouza.bancodigital.data.model.Recharge
import com.rpsouza.bancodigital.utils.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class RechargeDataSourceImpl @Inject constructor(
  database: FirebaseDatabase
) : IRechargeDataSource {

  private val rechargeReference = database.reference
    .child("recharge")
    .child(FirebaseHelper.getUserId())

  override suspend fun saveRecharge(recharge: Recharge): Recharge {
    return suspendCoroutine { continuation ->
      rechargeReference.child(recharge.id)
        .setValue(recharge).addOnCompleteListener { task ->
          if (task.isSuccessful) {
            val dateReference = rechargeReference
              .child(recharge.id)
              .child("date")

            dateReference.setValue(ServerValue.TIMESTAMP)
              .addOnCompleteListener { taskUpdate ->
                if (taskUpdate.isSuccessful) {
                  continuation.resumeWith(Result.success(recharge))
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

  override suspend fun getRecharge(idRecharge: String): Recharge {
    return suspendCoroutine { continuation ->
      rechargeReference.child(idRecharge)
        .addListenerForSingleValueEvent(object : ValueEventListener {
          override fun onDataChange(snapshot: DataSnapshot) {
            val recharge = snapshot.getValue(Recharge::class.java)

            recharge?.let { continuation.resumeWith(Result.success(it)) }
          }

          override fun onCancelled(error: DatabaseError) {
            error.toException().let {
              continuation.resumeWith(Result.failure(it))
            }
          }

        })
    }
  }
}