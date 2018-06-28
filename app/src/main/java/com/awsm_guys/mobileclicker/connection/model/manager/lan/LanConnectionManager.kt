package com.awsm_guys.mobileclicker.connection.model.manager.lan

import android.os.Build
import android.util.Log
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopController
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopControllerFactory
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.LanDesktopController
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.ClickerMessage
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.Header
import com.awsm_guys.mobileclicker.connection.model.manager.ConnectionManager
import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class LanConnectionManager: ConnectionManager {

    private val objectMapper = ObjectMapper().registerModule(KotlinModule())

    private val broadcastPort = 8841
    private val connectionPort = 5055
    private val broadcastIp = "255.255.255.255"

    private var senderSocket = DatagramSocket()
    private var connectionSocket = DatagramSocket(connectionPort)
    private val datagramPacket = DatagramPacket(ByteArray(2048), 2048)

    private val broadcastMessage by lazy {
            ObjectMapper().writeValueAsString(
                    ClickerMessage(
                            Header.CONNECT,
                            "abs ${Build.MANUFACTURER} ${Build.MODEL}",
                            mutableMapOf("port" to "17710")
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

    private val connectionSubject: PublishSubject<DesktopControllerFactory> = PublishSubject.create()
    private val isRunning = AtomicBoolean(false)

    override fun startListening(): Observable<DesktopControllerFactory> {
        isRunning.set(true)
        compositeDisposable.add(
            Observable.interval(500, TimeUnit.MILLISECONDS)
                    .repeatUntil(isRunning::get)
                    .map { broadcastPacket }
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        senderSocket::send
                    } , {
                        Log.e(this::class.simpleName, it.message)
                    })
        )

        compositeDisposable.add(
            Observable.fromCallable { connectionSocket.receive(datagramPacket) }
                    .repeatUntil(isRunning::get)
                    .map {
                        objectMapper.readValue(
                                String(datagramPacket.data), ClickerMessage::class.java
                        )
                    }
                    .retry()
                    .filter{ it.header == Header.OK }
                    .map {
                        object : DesktopControllerFactory() {
                            override fun create(primitiveStore: PrimitiveStore, sessionId: String): DesktopController =
                                    LanDesktopController(
                                            datagramPacket.address.hostAddress,
                                            it.body.toInt(),
                                            it.features["sessionId"] ?: throw Exception(
                                                    "Empty sessionId"),
                                            primitiveStore
                                    )
                        }
                    }
                    .retry()
                    .subscribe({
                        connectionSubject.onNext(it)
                    }, {
                        Log.e(this::class.simpleName, it.message)
                    })
        )

        return connectionSubject.hide()
    }

    override fun stopListening() {
        isRunning.set(false)
    }
}