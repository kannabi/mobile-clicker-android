package com.awsm_guys.mobileclicker.lan

import com.awsm_guys.mobileclicker.TestPrimitiveStore
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.LanDesktopControllerFactory
import com.awsm_guys.mobileclicker.primitivestore.IP_KEY
import com.awsm_guys.mobileclicker.primitivestore.MAX_PAGE_KEY
import com.awsm_guys.mobileclicker.primitivestore.PORT_KEY
import com.awsm_guys.mobileclicker.primitivestore.SESSION_ID_KEY
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import java.net.InetSocketAddress
import java.net.ServerSocket

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


}