package com.rpsouza.bancodigital.data.repository.transfer

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.rpsouza.bancodigital.data.model.Transfer
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransferDataSourceImpl @Inject constructor(
  database: FirebaseDatabase
) : ITransferDataSource {

  private val transferReference = database.reference
    .child("transfer")

  override suspend fun saveTransfer(transfer: Transfer) {
    return suspendCoroutine { continuation ->
      transferReference.child(transfer.idUserSent).child(transfer.id)
        .setValue(transfer).addOnCompleteListener { task ->
          if (task.isSuccessful) {

            transferReference.child(transfer.idUserReceived).child(transfer.id)
              .setValue(transfer).addOnCompleteListener { taskTransferReceived ->
                if (taskTransferReceived.isSuccessful) {
                  continuation.resumeWith(Result.success(Unit))
                } else {
                  taskTransferReceived.exception?.let {
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