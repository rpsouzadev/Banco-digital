package com.rpsouza.bancodigital.data.repository.transfer

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.rpsouza.bancodigital.data.enum.TransactionOperation
import com.rpsouza.bancodigital.data.enum.TransactionType
import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.data.model.Transfer
import com.rpsouza.bancodigital.utils.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransferDataSourceImpl @Inject constructor(
  database: FirebaseDatabase
) : ITransferDataSource {

  private val transferReference = database.reference
    .child("transfer")

  private val transactionReference = database.reference
    .child("transaction")

  override suspend fun getTransfer(idTransfer: String): Transfer {
    return suspendCoroutine { continuation ->
      transferReference.child(FirebaseHelper.getUserId()).child(idTransfer)
        .addListenerForSingleValueEvent(object : ValueEventListener {
          override fun onDataChange(snapshot: DataSnapshot) {
            val transfer = snapshot.getValue(Transfer::class.java)
            transfer?.let {
              continuation.resumeWith(Result.success(it))
            }
          }

          override fun onCancelled(error: DatabaseError) {
            continuation.resumeWith(Result.failure(error.toException()))
          }

        })
    }
  }

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

  override suspend fun saveTransferTransaction(transfer: Transfer) {
    return suspendCoroutine { continuation ->
      val transactionUserSend = Transaction(
        id = transfer.id,
        date = transfer.date,
        amount = transfer.amount,
        type = TransactionType.CASH_OUT,
        operation = TransactionOperation.TRANSFER,
      )

      val transactionUserReceived = Transaction(
        id = transfer.id,
        date = transfer.date,
        amount = transfer.amount,
        type = TransactionType.CASH_IN,
        operation = TransactionOperation.TRANSFER,
      )

      transactionReference.child(transfer.idUserSent).child(transfer.id)
        .setValue(transactionUserSend).addOnCompleteListener { taskUserSent ->
          if (taskUserSent.isSuccessful) {

            transactionReference.child(transfer.idUserReceived).child(transfer.id)
              .setValue(transactionUserReceived).addOnCompleteListener { taskUserReceived ->
                if (taskUserReceived.isSuccessful) {
                  continuation.resumeWith(Result.success(Unit))
                } else {
                  taskUserReceived.exception?.let {
                    continuation.resumeWith(Result.failure(it))
                  }
                }
              }

          } else {
            taskUserSent.exception?.let {
              continuation.resumeWith(Result.failure(it))
            }
          }
        }
    }
  }
}