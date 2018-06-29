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

class LanConnectionManager: ConnectionManager, LoggingMixin{

    private val objectMapper = ObjectMapper().registerModule(KotlinModule())

    private val broadcastPort = 8841
    private val connectionPort = 5055
    private val broadcastIp = "255.255.255.255"

    private var senderSocket = DatagramSocket()
    private var connectionSocket = DatagramSocket(connectionPort)
    private val datagramPacket = DatagramPacket(ByteArray(1024), 1024)

    private val broadcastMessage by lazy {
            ObjectMapper().writeValueAsString(
                    ClickerMessage(
                            Header.CONNECT,
                            "abs ${Build.MANUFACTURER} ${Build.MODEL}",
                            mutableMapOf("port" to connectionPort.toString())
                    )
            ).toByteArray()
    }

    private val broadcastPacket = DatagramPacket(
            broadcastMessage,
            broadcastMessage.size,
            InetAddress.getByName(broadcastIp),
            broadcastPort
    )

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val isRunning = AtomicBoolean(false)

    override fun startListening(): Observable<DesktopControllerFactory> {
        isRunning.set(true)
        compositeDisposable.add(
            Observable.interval(500, TimeUnit.MILLISECONDS)
                    .repeatUntil(isRunning::get)
                    .map { broadcastPacket }
                    .subscribeOn(Schedulers.io())
                    .subscribe(senderSocket::send, ::trace)
        )

            return Observable.fromCallable { connectionSocket.receive(datagramPacket) }
                    .repeatUntil(isRunning::get)
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
                                it.features["sessionId"] ?:
                                throw Exception("Empty sessionId")
                        ) as DesktopControllerFactory
                    }
                    .retry()
    }

    override fun stopListening() {
        isRunning.set(false)
        compositeDisposable.clear()
    }
}