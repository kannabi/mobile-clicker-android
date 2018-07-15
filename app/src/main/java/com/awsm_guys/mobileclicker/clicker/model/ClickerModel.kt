package com.awsm_guys.mobileclicker.clicker.model

import com.awsm_guys.mobileclicker.clicker.IClickerModel
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopController
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopControllerFactory
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopControllerFactoryGenerator
import com.awsm_guys.mobileclicker.clicker.model.events.*
import com.awsm_guys.mobileclicker.primitivestore.CONTROLLER_TAG_KEY
import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore
import com.awsm_guys.mobileclicker.utils.LoggingMixin
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ClickerModel(
        private val primitiveStore: PrimitiveStore,
        private val controllerFactory: DesktopControllerFactory? = null
) : IClickerModel, LoggingMixin {

    private val clickerEventSubject = BehaviorSubject.create<ClickerEvent>()
    private val controllerFactoryGenerator by lazy { DesktopControllerFactoryGenerator() }
    private lateinit var desktopController: DesktopController
    private val compositeDisposable = CompositeDisposable()

    private var currentPage: Int = 0


    override fun connect(): Observable<ClickerEvent> =
        Observable.fromCallable {
            desktopController =
                    (controllerFactory ?:
                        controllerFactoryGenerator.generate(
                                primitiveStore.find(CONTROLLER_TAG_KEY)!!
                        )!!
                    ).create(primitiveStore)

            desktopController.init()
            subscribeToDesktopController()
        }
        .doOnNext{ clickerEventSubject.onNext(ConnectionOpen(desktopController.getPageNumbers())) }
        .flatMap { clickerEventSubject.hide() }

    override fun disconnect() {
        desktopController.disconnect()
        compositeDisposable.clear()
    }

    private fun subscribeToDesktopController() {
        compositeDisposable.add(
                desktopController.getPageSwitchingObservable()
                        .subscribeOn(Schedulers.io())
                        .map {
                            currentPage = it
                            PageSwitch(it)
                        }
                        .subscribe(clickerEventSubject::onNext, {
                            trace(it)
                            clickerEventSubject.onNext(ClickerBroken())
                        }, {
                            clickerEventSubject.onNext(ConnectionClose())
                        })
        )
    }

    override fun switchPage(number: Int) {
        desktopController.switchPage(number)
    }

    override fun onNextClick() {
        switchPage(currentPage + 1)
    }

    override fun onPreviousClick() {
        switchPage(currentPage - 1)
    }
}