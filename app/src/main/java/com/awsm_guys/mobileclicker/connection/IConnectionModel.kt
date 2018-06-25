package com.awsm_guys.mobileclicker.connection

import io.reactivex.Single

interface IConnectionModel {
    fun saveUsername(username: String)

    fun startConnection()

    fun stopConnection()

    fun getCurrentUsername(): Single<String>
}