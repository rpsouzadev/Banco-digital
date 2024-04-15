package com.rpsouza.bancodigital.utils

import com.google.firebase.auth.FirebaseAuth

class FirebaseHelper {

  companion object {
    fun isAuthenticated() = FirebaseAuth.getInstance().currentUser != null
  }


}