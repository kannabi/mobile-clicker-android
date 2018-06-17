package com.awsm_guys.mobileclicker.clicker.model.localnetwork

import android.os.Build
import com.awsm_guys.mobileclicker.clicker.model.localnetwork.poko.ClickerMessage
import com.awsm_guys.mobileclicker.clicker.model.localnetwork.poko.Header
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.concurrent.TimeUnit

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

    fun runBroadcast() {
        compositeDisposable.add (
            Observable.interval(1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::sendBroadcast)
        )
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