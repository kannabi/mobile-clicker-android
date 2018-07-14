package com.awsm_guys.mobileclicker.connection.model.manager.lan

import android.os.Build
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopControllerFactory
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.LanDesktopControllerFactory
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.ClickerMessage
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.Header
import com.awsm_guys.mobileclicker.connection.model.manager.ConnectionManager
import com.awsm_guys.mobileclicker.utils.LoggingMixin
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class LanConnectionManager: ConnectionManager, LoggingMixin {

    private val objectMapper = ObjectMapper().registerModule(KotlinModule())

    private val broadcastPort = 8841
    private val connectionPort = 5055
    private val broadcastIp = "255.255.255.255"

    private var _name = ""

    private var senderSocket = DatagramSocket().apply { reuseAddress = true }
    private var connectionSocket = DatagramSocket(connectionPort).apply { reuseAddress = true }
    private val datagramPacket = DatagramPacket(ByteArray(2048), 2048)

    private lateinit var broadcastMessage: ByteArray

    private val broadcastPacket by lazy {
        DatagramPacket(
            broadcastMessage,
            broadcastMessage.size,
            InetAddress.getByName(broadcastIp),
            broadcastPort
        )
    }

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val isRunning = AtomicBoolean(false)

    override fun startListening(): Observable<DesktopControllerFactory> {
        isRunning.set(true)
        compositeDisposable.add(
            Observable.interval(500, TimeUnit.MILLISECONDS)
                    .map { broadcastPacket }
                    .subscribeOn(Schedulers.io())
                    .doOnNext { log("send packet ${String(broadcastPacket.data)}") }
                    .subscribe(senderSocket::send, ::trace)
        )

            return Observable.fromCallable { connectionSocket.receive(datagramPacket) }
                    .map {
                        objectMapper.readValue(
                                String(datagramPacket.data), ClickerMessage::class.java
                        )
                    }
                    .retry()
                    .filter { it.header == Header.OK }
                    .map {
                        LanDesktopControllerFactory(
                                datagramPacket.address.hostAddress,
                                it.body.toInt(),
                                it.features["sessionId"]
                                        ?: throw Exception("Empty sessionId"),
                                it.features["maxPage"]?.toInt()
                                        ?: throw Exception("Empty or invalid maxPage")
                        ) as DesktopControllerFactory
                    }
                    .doOnNext { println("controller generated") }
                    .retry()
    }

    override fun stopListening() {
        isRunning.set(false)
        compositeDisposable.clear()
    }

    override fun setName(name: String) {
        _name = name
        broadcastMessage = getBroadcastMessageByteArray()
    }

    private fun getBroadcastMessageByteArray() =
            ObjectMapper().writeValueAsString(
                    ClickerMessage(
                            Header.CONNECT,
                            "$_name ${Build.MANUFACTURER} ${Build.MODEL}",
                            mutableMapOf("port" to connectionPort.toString())
                    )
            ).toByteArray()

}