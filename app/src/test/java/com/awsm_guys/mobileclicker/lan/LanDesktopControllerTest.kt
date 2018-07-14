package com.awsm_guys.mobileclicker.lan

import com.awsm_guys.mobileclicker.TestPrimitiveStore
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.LanDesktopControllerFactory
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.RxSocketWrapper
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.ClickerMessage
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.Header
import com.awsm_guys.mobileclicker.primitivestore.IP_KEY
import com.awsm_guys.mobileclicker.primitivestore.MAX_PAGE_KEY
import com.awsm_guys.mobileclicker.primitivestore.PORT_KEY
import com.awsm_guys.mobileclicker.primitivestore.SESSION_ID_KEY
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.util.concurrent.TimeUnit

class LanDesktopControllerTest {

    @Test
    fun testRestoring() {
        val store = TestPrimitiveStore()

        var isCrashed = false
        try {
            LanDesktopControllerFactory().create(store).init()
        } catch (e: Throwable) {
            isCrashed = true
        }

        assert(isCrashed)

        store.store(
            mapOf(
                IP_KEY to "localhost",
                PORT_KEY to "8666",
                SESSION_ID_KEY to "jhsfvakjs",
                MAX_PAGE_KEY to "15"
            )
        )

        Single.fromCallable {
            ServerSocket().use {
                it.reuseAddress = true
                it.bind(InetSocketAddress(8666))
                it.accept()
            }
        }.subscribeOn(Schedulers.io()).subscribe()
        var noCrash = true
        try {
            LanDesktopControllerFactory().create(store).init()
        } catch (e: Throwable) {
            noCrash = false
        }

        assert(noCrash)
    }

    @Test
    fun testTrashReceive() {
        val controller = LanDesktopControllerFactory().create(
                TestPrimitiveStore().apply {
                    store(
                        mapOf(
                                IP_KEY to "127.0.0.1",
                                PORT_KEY to "8666",
                                SESSION_ID_KEY to "jhsfvakjs",
                                MAX_PAGE_KEY to "15"
                        )
                    )
                }
        )

        val unblockSubject = PublishSubject.create<Unit>()

        Single.just(unblockSubject)
                .map(::imitateDesktop)
                .subscribeOn(Schedulers.io()).subscribe()

        controller.init()

        assert(42 == controller.getPageSwitchingObservable().blockingFirst())
        unblockSubject.onNext(Unit)
    }

    private fun imitateDesktop(unblockObservable: Observable<Unit>) {
        val objectMapper = jacksonObjectMapper()
        val rxSocketWrapper: RxSocketWrapper =
            ServerSocket().use {
                it.reuseAddress = true
                it.bind(InetSocketAddress(8666))
                RxSocketWrapper(it.accept())
            }

        Thread.sleep(2000)

        rxSocketWrapper.sendData(
                "From fairest creatures we desire increase, " +
                        "That thereby beauty's rose might never die, " +
                        "But as the riper should by time decease, " +
                        "His tender heir might bear his memory: " +
                        "But thou, contracted to thine own bright eyes"
        )

        rxSocketWrapper.sendData(
                objectMapper.writeValueAsString(
                        ClickerMessage(
                            Header.SWITCH_PAGE, "Post hoc, ergo propter hoc", mutableMapOf()
                        )
                )
        )

        rxSocketWrapper.sendData(
                objectMapper.writeValueAsString(
                        ClickerMessage(
                                Header.SWITCH_PAGE, "42", mutableMapOf()
                        )
                )
        )

        unblockObservable.toFuture().get(3600, TimeUnit.SECONDS)
        rxSocketWrapper.close()
    }
}