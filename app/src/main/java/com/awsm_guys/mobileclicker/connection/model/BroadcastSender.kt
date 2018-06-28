package com.awsm_guys.mobileclicker.connection.model

import android.os.Build
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.ClickerMessage
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.Header
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.concurrent.atomic.AtomicBoolean

class BroadcastSender {
    private val broadcastMessage =
            ObjectMapper().writeValueAsString(
                    ClickerMessage(
                            Header.CONNECT,
                            "abs ${Build.MANUFACTURER} ${Build.MODEL}",
                            mutableMapOf("port" to "17710")
                    )
            ).toByteArray()
    private val broadcastPort = 8841
    private val broadcastIp = "255.255.255.255"

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val publishSubject: PublishSubject<Unit> = PublishSubject.create()

    private val isRunning = AtomicBoolean(false)

    fun runBroadcast() {
        while (isRunning.get()) {

        }

    }

    private fun sendBroadcast(num: Long) {
        println("send message $num")
        DatagramSocket().use {
            it.send(
                DatagramPacket(
                    broadcastMessage,
                    broadcastMessage.size,
                    InetAddress.getByName(broadcastIp),
                    broadcastPort
                )
            )
        }
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }
}