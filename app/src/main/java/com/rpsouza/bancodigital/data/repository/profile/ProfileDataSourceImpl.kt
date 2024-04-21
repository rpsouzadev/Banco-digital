package com.rpsouza.bancodigital.data.repository.profile

import com.google.firebase.database.FirebaseDatabase
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.utils.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class ProfileDataSourceImpl @Inject constructor(
  database: FirebaseDatabase
) : IProfileDataSource {

  private val profileReference = database.reference
    .child("profile")
    .child(FirebaseHelper.getUserId())

  override suspend fun saveProfile(user: User) {
    return suspendCoroutine { continuation ->
      profileReference.setValue(user)
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