package com.awsm_guys.mobileclicker.clicker.model.controller.lan

import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopController
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.ClickerMessage
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.Header
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.Header.*
import com.awsm_guys.mobileclicker.clicker.model.controller.poko.Meta
import com.awsm_guys.mobileclicker.clicker.model.controller.poko.Page
import com.awsm_guys.mobileclicker.primitivestore.*
import com.awsm_guys.mobileclicker.utils.LoggingMixin
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException

class LanDesktopController(
        private var desktopIp: String? = null,
        private var desktopPort: Int? = null,
        private var sessionId: String? = null,
        private var primitiveStore: PrimitiveStore,
        private var maxPage: Int? = null,
        private var currentPage: Int = 0
): DesktopController, LoggingMixin {

    private val objectMapper by lazy { jacksonObjectMapper() }
    private lateinit var rxSocketWrapper: RxSocketWrapper
    private var inited = false

    private val updateMetaSubject = BehaviorSubject.create<Meta>()

    override fun init() {
        println("controller init")
        if (desktopIp == null || desktopPort == null ||
                sessionId == null || maxPage == null) {
            println("try to restore")
            restoreConnectionData()
        } else {
            primitiveStore.store(mapOf(
                    IP_KEY to desktopIp!!,
                    PORT_KEY to desktopPort!!.toString(),
                    SESSION_ID_KEY to sessionId!!,
                    MAX_PAGE_KEY to maxPage!!.toString()
            ))
        }
        rxSocketWrapper = RxSocketWrapper(
                Socket().apply {
                    try {
                        connect(
                            InetSocketAddress(desktopIp!!, desktopPort!!),
                            2000
                        )
                    } catch (timeoutException: SocketTimeoutException) {
                        close()
                        throw timeoutException
                    }
                }
        )

        inited = true
    }

    private fun restoreConnectionData() {
        desktopIp = primitiveStore.find(IP_KEY)
        desktopPort = primitiveStore.find(PORT_KEY)?.toInt()
        sessionId = primitiveStore.find(SESSION_ID_KEY)
        maxPage = primitiveStore.find(MAX_PAGE_KEY)?.toInt()
        println("restored $desktopIp $desktopPort $sessionId $maxPage")
        if (desktopIp == null || desktopPort == null || sessionId == null || maxPage == null)
            throw Exception("Cannot restore desktop ip and port")
    }

    override fun getPageNumbers(): Int {
        return maxPage!!
    }

    override fun getCurrentPage(): Int {
        return currentPage
    }

    override fun getPageSwitchingObservable(): Observable<Int> =
            rxSocketWrapper.inputObservable
                    .map { objectMapper.readValue(it, ClickerMessage::class.java) }
                    .retry()
                    //looks like a gross shit. Rewrite it!
                    .doOnNext { if (it.header == UPDATE_META) emitMetaUpdate(it) }
                    .filter { it.header == SWITCH_PAGE }
                    .map { it.body.toInt() }
                    .doOnNext(::currentPage::set)
                    .retry()

    override fun disconnect() {
        if (inited) {
            rxSocketWrapper.sendData(
                objectMapper.writeValueAsString(ClickerMessage(DISCONNECT, "", mutableMapOf()))
            ) {
                rxSocketWrapper.close()
                inited = false
            }
        }
    }

    override fun switchPage(page: Int) {
        if (page in 0 until maxPage!!) {
            rxSocketWrapper.sendData(
                    getMessage(Header.SWITCH_PAGE, page.toString(), mutableMapOf())
            )
        }
    }

    private fun getMessage(header: Header, body: String, features: MutableMap<String, String>) =
            objectMapper.writeValueAsString(ClickerMessage(header, body, features))

    private fun emitMetaUpdate(message: ClickerMessage) {
        try {
            updateMetaSubject.onNext(
                    with(message.features) {
                        Meta(
                                this["maxPage"]!!.toInt().also(::maxPage::set),
                                (1..maxPage!!)
                                        .map(Int::toString)
                                        .map { Page(it, this[it] ?: "") }
                        )
                    }
            )
        } catch (e: Exception) {
            log("Wrong meta\n$message")
            trace(e)
        }
    }


    override fun getMetaUpdateObservable(): Observable<Meta> = updateMetaSubject.hide()

    override fun isConnected() = rxSocketWrapper.isConnected()
}