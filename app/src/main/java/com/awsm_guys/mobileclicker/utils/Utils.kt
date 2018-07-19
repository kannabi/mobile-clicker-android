package com.awsm_guys.mobileclicker.utils

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiConsumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun wrapClickListening(compositeDisposable: CompositeDisposable,
                       view: View, onNext: ((t: Any) -> Unit)) {
    compositeDisposable.add(
            RxView.clicks(view)
                    .throttleWithTimeout(150, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onNext)
    )
}

fun makeSingle(compositeDisposable: CompositeDisposable, body: (() -> Unit)) {
    compositeDisposable.add(
            Single.fromCallable(body).subscribeOn(Schedulers.io()).subscribe()
    )
}

fun <T> makeSingle(compositeDisposable: CompositeDisposable, body: (() -> Unit),
                   onCallback: BiConsumer<in Any, in Throwable>) {
    compositeDisposable.add(
            Single.fromCallable(body).subscribeOn(Schedulers.io()).subscribe(onCallback)
    )
}