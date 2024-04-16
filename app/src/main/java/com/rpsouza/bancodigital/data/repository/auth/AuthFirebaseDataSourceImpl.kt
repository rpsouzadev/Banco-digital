package com.rpsouza.bancodigital.data.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.rpsouza.bancodigital.data.model.User
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class AuthFirebaseDataSourceImpl @Inject constructor(
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
    name: String,
    phone: String,
    email: String,
    password: String
  ): User {

    return suspendCoroutine { continuation ->
      firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
          if (task.isSuccessful) {
            val userId = task.result.user?.uid ?: ""
            val user = User(
              id = userId,
              name = name,
              email = email,
              phone = phone
            )

            continuation.resumeWith(Result.success(user))
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