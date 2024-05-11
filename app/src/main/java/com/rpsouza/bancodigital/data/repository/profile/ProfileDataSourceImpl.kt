package com.rpsouza.bancodigital.data.repository.profile

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.utils.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class ProfileDataSourceImpl @Inject constructor(
  database: FirebaseDatabase,
  storage: FirebaseStorage
) : IProfileDataSource {

  private val profileDatabaseReference = database.reference
    .child("profile")

  private val profileStorageReference = storage.reference
    .child("images")
    .child("profiles")
    .child("${FirebaseHelper.getUserId()}.jpeg")

  override suspend fun saveProfile(user: User) {
    return suspendCoroutine { continuation ->
      profileDatabaseReference.child(FirebaseHelper.getUserId())
        .setValue(user).addOnCompleteListener { task ->
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

  override suspend fun getProfile(): User {
    return suspendCoroutine { continuation ->
      profileDatabaseReference.child(FirebaseHelper.getUserId())
        .addListenerForSingleValueEvent(
          object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              val profile = snapshot.getValue(User::class.java)

              profile?.let { continuation.resumeWith(Result.success(it)) }
            }

            override fun onCancelled(error: DatabaseError) {
              continuation.resumeWith(Result.failure(error.toException()))
            }

          })
    }
  }

  override suspend fun getProfileList(): List<User> {
    return suspendCoroutine { continuation ->
      profileDatabaseReference.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(snapshot: DataSnapshot) {
            val profileList = mutableListOf<User>()

            for (ds in snapshot.children) {
              val profile = ds.getValue(User::class.java)

              profile?.let { profileList.add(it) }
            }

            continuation.resumeWith(Result.success(
              profileList.apply {
                removeIf { it.id == FirebaseHelper.getUserId() }
              }
            ))
          }

          override fun onCancelled(error: DatabaseError) {
            continuation.resumeWith(Result.failure(error.toException()))
          }

        }
      )
    }
  }

  override suspend fun saveImage(imageProfile: String): String {
    return suspendCoroutine { continuation ->
      val uploadTask = profileStorageReference.putFile(Uri.parse(imageProfile))
      uploadTask.addOnSuccessListener {

        profileStorageReference.downloadUrl.addOnCompleteListener { task ->
          continuation.resumeWith(Result.success(task.result.toString()))
        }

      }.addOnFailureListener {
        continuation.resumeWith(Result.failure(it))
      }
    }
  }
}