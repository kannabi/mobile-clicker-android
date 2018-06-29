package com.awsm_guys.mobileclicker.connection.presenter

import android.content.Context
import android.content.Intent
import android.util.Log
import com.awsm_guys.mobileclicker.MainActivity
import com.awsm_guys.mobileclicker.connection.IConnectionModel
import com.awsm_guys.mobileclicker.connection.IConnectionPresenter
import com.awsm_guys.mobileclicker.connection.IConnectionView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class ConnectionPresenter(
        private val model: IConnectionModel,
        private val context: Context
) : IConnectionPresenter {

    private val logTag by lazy { this::class.simpleName }
    private val compositeDisposable = CompositeDisposable()

    private var view: IConnectionView? = null

    private var currentState: State = State.NAME_ENTER

    override fun attachView(view: IConnectionView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun onViewReady() {
        when (currentState){
            State.NAME_ENTER ->
                compositeDisposable.add(
                        model.getCurrentUsername()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { name, error ->
                                    if (name != null) {
                                        view?.showAskNameWindow(name)
                                    } else {
                                        Log.e(logTag, error.message)
                                        view?.showAskNameWindow()
                                    }
                                }
                )
            State.SEARCHING -> Unit
        }
    }

    override fun onCancelSearch() {
    }

    override fun onUsernameEnter(username: String) {
        compositeDisposable.add(
            Observable.fromCallable { model.saveUsername(username) }
                    .doOnNext {
                        view?.showSearch()
                        currentState = State.SEARCHING
                    }
                    .flatMap { model.startConnection() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                    }, {
                        Log.e(logTag, it.message)
                        view?.showConnectionError()
                    }, {
                        startClicker()
                    })
        )
    }

    private fun startClicker() {
        context.startActivity(
                Intent(context, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    override fun onDestroy() {
        model.onDestroy()
        compositeDisposable.clear()
    }

    private enum class State {
        NAME_ENTER, SEARCHING
    }
}