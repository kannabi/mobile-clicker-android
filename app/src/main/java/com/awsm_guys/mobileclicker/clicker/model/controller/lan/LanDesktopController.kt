package com.awsm_guys.mobileclicker.clicker.model.controller.lan

import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopController
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.ClickerMessage
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.Header
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.Header.SWITCH_PAGE
import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore
import com.awsm_guys.mobileclicker.primitivestore.SESSION_ID_KEY
import com.awsm_guys.mobileclicker.utils.LoggingMixin
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable
import java.net.InetSocketAddress
import java.net.Socket

class LanDesktopController(
        private var desktopIp: String? = null,
        private var desktopPort: Int? = null,
        private var sessionId: String? = null,
        private var primitiveStore: PrimitiveStore,
        private var maxPage: Int = 0,
        private var currentPage: Int = 0
): DesktopController, LoggingMixin {

    private val IP_KEY = "IP"
    private val PORT_KEY = "PORT"

    private val objectMapper by lazy { ObjectMapper() }
    private lateinit var rxSocketWrapper: RxSocketWrapper

    override fun init() {
        println("controller init")
        if (desktopIp == null || desktopPort == null || sessionId == null) {
            restoreConnectionData()
        } else {
            primitiveStore.store(mapOf(
                    IP_KEY to desktopIp!!,
                    PORT_KEY to desktopPort!!.toString(),
                    SESSION_ID_KEY to sessionId!!
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
    }

    private fun restoreConnectionData() {
        desktopIp = primitiveStore.find(IP_KEY)
        desktopPort = primitiveStore.find(PORT_KEY)?.toInt()
        sessionId = primitiveStore.find(SESSION_ID_KEY)
        if (desktopIp != null && desktopPort != null && sessionId != null)
            throw Exception("Cannot restore desktop ip and port")
    }

    override fun getPageNumbers(): Int {
        return maxPage
    }

    override fun getCurrentPage(): Int {
        return currentPage
    }

    override fun getPageSwitchingObservable(): Observable<Int> =
            rxSocketWrapper.inputObservable
                    .map { objectMapper.readValue(it, ClickerMessage::class.java) }
                    .retry()
                    .filter { it.header == SWITCH_PAGE }
                    .map { it.body.toInt() }
                    .retry()

    override fun disconnect() {
        rxSocketWrapper.close()
    }

    override fun switchPage(page: Int) {
        rxSocketWrapper.sendData(
                getMessage(Header.SWITCH_PAGE, page.toString(), mutableMapOf())
        )
    }

    private fun getMessage(header: Header, body: String, features: MutableMap<String, String>) =
            objectMapper.writeValueAsString(ClickerMessage(header, body, features))
}