package com.awsm_guys.mobileclicker.clicker.model.controller.localnetwork

import com.awsm_guys.mobile_clicker.mobile.localnetwork.RxSocketWrapper
import com.awsm_guys.mobileclicker.SESSION_ID_KEY
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopController
import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore
import io.reactivex.Observable
import java.net.InetSocketAddress
import java.net.Socket

class LocalNetworkDesktopController(
        private var  desktopIp: String? = null,
        private var desktopPort: Int? = null,
        private var sessionId: String,
        private var primitiveStore: PrimitiveStore
): DesktopController {

    private val IP_KEY = "IP"
    private val PORT_KEY = "PORT"


    private lateinit var rxSocketWrapper: RxSocketWrapper

    override fun init(): Observable<DesktopController> =
        Observable.fromCallable {
            if ( !((desktopIp == null || desktopPort == null) && restoreConnectionData()) ){
                throw Exception("Cannot restore desktop ip and port")
            } else {
                primitiveStore.store(mapOf(
                        IP_KEY to desktopIp!!,
                        PORT_KEY to desktopPort!!.toString(),
                        SESSION_ID_KEY to sessionId
                ))
            }
            rxSocketWrapper = RxSocketWrapper(
                Socket().apply {
                    connect(
                        InetSocketAddress(desktopIp!!, desktopPort!!),
                        2000
                    )
                }
            )
        }.map { this }


    private fun restoreConnectionData(): Boolean {
        desktopIp = primitiveStore.find(IP_KEY)
        desktopPort = primitiveStore.find(PORT_KEY)?.toInt()
        return desktopIp != null && desktopPort != null
    }

    override fun getPageNumbers(): Int {
        return 1
    }

    override fun getCurrentPage(): Int {
        return 1
    }

    override fun switchPage(page: Int) {

    }

    override fun getPageSwitchingObservable(): Observable<Int> {
        return Observable.empty<Int>()
    }

    override fun disconnect() {
    }
}