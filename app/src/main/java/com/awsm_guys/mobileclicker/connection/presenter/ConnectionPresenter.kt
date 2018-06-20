package com.awsm_guys.mobileclicker.connection.presenter

import android.content.Context
import com.awsm_guys.mobileclicker.connection.IConnectionModel
import com.awsm_guys.mobileclicker.connection.IConnectionPresenter
import com.awsm_guys.mobileclicker.connection.IConnectionView
import io.reactivex.disposables.CompositeDisposable

class ConnectionPresenter(
        private val iConnectionModel: IConnectionModel,
        private val context: Context
) : IConnectionPresenter {

    private val compositeDisposable = CompositeDisposable()

    private var view: IConnectionView? = null

    override fun attachView(view: IConnectionView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }

    override fun onViewReady() {

    }
}