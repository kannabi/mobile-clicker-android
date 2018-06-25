package com.awsm_guys.mobileclicker.connection.model

import com.awsm_guys.mobileclicker.CURRENT_NAME_KEY
import com.awsm_guys.mobileclicker.connection.IConnectionModel
import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore
import io.reactivex.Single

class ConnectionModel(
        private var primitiveStore: PrimitiveStore
) : IConnectionModel {

    private var currentUsername: String? = null

    override fun saveUsername(username: String) {
        if (currentUsername != username) {
            primitiveStore.store(CURRENT_NAME_KEY, username)
            currentUsername = username
        }
    }

    override fun startConnection() {

    }

    override fun stopConnection() {

    }

    override fun getCurrentUsername(): Single<String> =
            Single.just(
                    currentUsername ?:
                            primitiveStore.find(CURRENT_NAME_KEY).also(::currentUsername::set) ?:
                            ""
            )
}