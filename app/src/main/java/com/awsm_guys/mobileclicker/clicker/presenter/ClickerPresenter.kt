package com.awsm_guys.mobileclicker.clicker.presenter

import android.content.Context
import android.content.Intent
import com.awsm_guys.mobileclicker.clicker.IClickerModel
import com.awsm_guys.mobileclicker.clicker.IClickerPresenter
import com.awsm_guys.mobileclicker.clicker.IClickerView
import com.awsm_guys.mobileclicker.clicker.model.events.*
import com.awsm_guys.mobileclicker.connection.view.ConnectionActivity
import com.awsm_guys.mobileclicker.utils.LoggingMixin
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class ClickerPresenter(
        private val context: Context,
        private val model: IClickerModel
): IClickerPresenter, LoggingMixin {

    private val compositeDisposable = CompositeDisposable()
    private var view: IClickerView? = null

    override fun attachView(view: IClickerView) {
        this.view = view
    }

    override fun onViewReady() {
        view?.showConnectionProcess()
        compositeDisposable.add(startConnection())
    }

    override fun detachView() {
        view = null
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }

    private fun startConnection(): Disposable =
            model.connect()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(::processClickerEvent){
                        trace(it)
                        view?.showConnectionLossDialog()
                    }

    private fun processClickerEvent(event: ClickerEvent) {
        when(event) {
            is ConnectionClose -> onGoToConnection()
            is ClickerBroken -> view?.showConnectionLossDialog()
            is PageSwitch -> view?.updateCurrentPage(event.page)
            is ConnectionOpen -> {
                view?.apply {
                    showConnectionEstablished()
                    updateMaxPage(event.maxPage)
                }
            }
        }
    }

    override fun onGoToConnection() {
        context.startActivity(
                Intent(context, ConnectionActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    override fun onPageSwitching(number: Int) = model.switchPage(number)

    override fun onNextClick() = model.onNextClick()

    override fun onPreviousClick() = model.onPreviousClick()
}