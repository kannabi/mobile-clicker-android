package com.awsm_guys.mobileclicker.clicker.view

import android.view.KeyEvent
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.disposables.CompositeDisposable

class VolumeButtonBroadcastDelegate {

    private val compositeDisposable = CompositeDisposable()
    private val emmitersList: MutableList<ObservableEmitter<Int>> = mutableListOf()

    private val observable by lazy {
        Observable.create { emitter: ObservableEmitter<Int> ->  emmitersList.add(emitter)}
    }

    fun getPressObservable(): Observable<Int> = observable

    fun onKeyPressed(key: Int): Boolean =
        if (key == KeyEvent.KEYCODE_VOLUME_DOWN || key == KeyEvent.KEYCODE_VOLUME_UP) {
            emmitersList.forEach { it.onNext(key) }
            true
        } else {
            false
        }

    fun onDestroy() {
        compositeDisposable.clear()
    }

    interface VolumeButtonsObservable {
        fun getObservable(): Observable<Int>
    }
}