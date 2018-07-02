package com.awsm_guys.mobileclicker.utils

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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