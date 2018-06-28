package com.awsm_guys.mobileclicker.clicker.model

import com.awsm_guys.mobileclicker.CONTROLLER_TAG_KEY
import com.awsm_guys.mobileclicker.SESSION_ID_KEY
import com.awsm_guys.mobileclicker.clicker.IClickerModel
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopController
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopControllerFactory
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopControllerFactoryGenerator
import com.awsm_guys.mobileclicker.clicker.model.events.ClickerBroken
import com.awsm_guys.mobileclicker.clicker.model.events.ClickerEvent
import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class ClickerModel(
        private val primitiveStore: PrimitiveStore,
        private var controllerFactory: DesktopControllerFactory? = null
) : IClickerModel {
    private val clickerEventSubject = BehaviorSubject.create<ClickerEvent>()
    private val controllerFactoryGenerator by lazy { DesktopControllerFactoryGenerator() }
    private lateinit var desktopController: DesktopController


    override fun connect(): Observable<ClickerEvent> =
        Observable.fromCallable {
            val sessionId = primitiveStore.find(SESSION_ID_KEY)!!
            val controllerTag = primitiveStore.find(CONTROLLER_TAG_KEY)!!

            val desktopController =
                    (controllerFactory ?: controllerFactoryGenerator.generate(controllerTag))
                            ?.create(primitiveStore, sessionId)!!

            this.desktopController = desktopController.apply { init() }
        }
        .doOnError { clickerEventSubject.onNext(ClickerBroken()) }
        .flatMap { clickerEventSubject.hide() }

    override fun disconnect() {
    }
}