package com.rpsouza.bancodigital.utils

sealed class StateView<T>(val data: T? = null, val message: String? = null) {

  class Loading<T> : StateView<T>(null, null)

  class Error<T>(message: String?) : StateView<T>(message = message)

  class Success<T>(data: T?, message: String? = null) : StateView<T>(data, message)
}