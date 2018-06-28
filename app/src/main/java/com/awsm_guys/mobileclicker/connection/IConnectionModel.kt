package com.awsm_guys.mobileclicker.connection

import io.reactivex.Observable
import io.reactivex.Single

interface IConnectionModel {
    fun saveUsername(username: String)

    fun startConnection(): Observable<Unit>

    fun stopConnection()

    fun getCurrentUsername(): Single<String>

    fun onDestroy()
}