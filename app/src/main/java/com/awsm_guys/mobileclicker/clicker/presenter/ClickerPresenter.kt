package com.awsm_guys.mobileclicker.clicker.presenter

import android.content.Context
import com.awsm_guys.mobileclicker.clicker.IClickerModel
import com.awsm_guys.mobileclicker.clicker.IClickerPresenter
import com.awsm_guys.mobileclicker.clicker.IClickerView
import io.reactivex.disposables.CompositeDisposable


class ClickerPresenter(
        private val context: Context,
        private val model: IClickerModel
): IClickerPresenter {

    private val compositeDisposable = CompositeDisposable()
    private var view: IClickerView? = null

    override fun attachView(view: IClickerView) {
        this.view = view
    }

    override fun onViewReady() {

    }

    override fun detachView() {
        view = null
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }
}