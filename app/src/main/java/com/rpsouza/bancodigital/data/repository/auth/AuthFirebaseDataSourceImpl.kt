package com.rpsouza.bancodigital.data.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlin.coroutines.suspendCoroutine

class AuthFirebaseDataSourceImpl(
  private val firebaseDatabase: FirebaseDatabase,
  private val firebaseAuth: FirebaseAuth
) : IAuthFirebaseDataSource {
  override suspend fun login(email: String, password: String) {
    return suspendCoroutine { continuation ->
      firebaseAuth.signInWithEmailAndPassword(email, password)
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

  override suspend fun register(
    name: String, email: String, phone: String, password: String
  ): FirebaseUser {
    return suspendCoroutine { continuation ->
      firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
          if (task.isSuccessful) {
            val user = task.result.user

            user?.let {
              continuation.resumeWith(Result.success(it))
            }
          } else {
            task.exception?.let {
              continuation.resumeWith(Result.failure(it))
            }
          }
        }
    }
  }

  override suspend fun recover(email: String) {
    return suspendCoroutine { continuation ->
      firebaseAuth.sendPasswordResetEmail(email)
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