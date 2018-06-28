
package com.awsm_guys.mobileclicker.connection.model

import android.content.Context
import com.awsm_guys.mobileclicker.CURRENT_NAME_KEY
import com.awsm_guys.mobileclicker.connection.IConnectionModel
import com.awsm_guys.mobileclicker.connection.model.manager.ConnectionManager
import com.awsm_guys.mobileclicker.connection.model.manager.lan.LanConnectionManager
import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ConnectionModel(
        private var primitiveStore: PrimitiveStore,
        private val context: Context
) : IConnectionModel {

    private var currentUsername: String? = null

    private val broadcastManager: ConnectionManager by lazy { LanConnectionManager() }

    private var managerDisposable: Disposable? = null
    private val connectionSubject = PublishSubject.create<Unit>()

    override fun saveUsername(username: String) {
        if (currentUsername != username) {
            primitiveStore.store(CURRENT_NAME_KEY, username)
            currentUsername = username
        }
    }

    override fun startConnection(): Observable<Unit> {
        managerDisposable =
            broadcastManager.startListening()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        connectionSubject.onComplete()
                    }, {
                        connectionSubject.onError(it)
                    })

        return connectionSubject.hide()
    }

    override fun stopConnection() {
        broadcastManager.stopListening()
        managerDisposable?.dispose()
    }

    override fun getCurrentUsername(): Single<String> =
            Single.just(
                    currentUsername ?:
                            primitiveStore.find(CURRENT_NAME_KEY).also(::currentUsername::set) ?:
                            ""
            )

    override fun onDestroy() {
        managerDisposable?.dispose()
    }
}