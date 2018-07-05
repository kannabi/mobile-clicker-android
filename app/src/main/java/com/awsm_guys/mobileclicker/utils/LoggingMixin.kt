package com.awsm_guys.mobileclicker.utils

import android.util.Log

interface LoggingMixin {
    fun trace(error: Throwable){
        Log.e("Clicker ${this::class.simpleName}", error.message ?: "EXCEPTION")
    }

    fun log(message: String) {
        Log.d("Clicker ${this::class.simpleName}", message)
    }
}