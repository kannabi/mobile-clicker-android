package com.awsm_guys.mobileclicker.lan

import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopControllerFactory
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.LanDesktopControllerFactory
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.ClickerMessage
import com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko.Header
import com.awsm_guys.mobileclicker.connection.model.manager.lan.LanConnectionManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class LanConnectionManagerTest {

    @Test
    fun testReceivingPackage() {
        val testSessionId = "fskjabfjhv"
        val testMaxPage = "15"
        val testPort = "1000"

        val objectMapper = jacksonObjectMapper()

        val factorySubject = BehaviorSubject.create<DesktopControllerFactory>()

        val manager = LanConnectionManager().apply {
            startListening()
                    .subscribeOn(Schedulers.io())
                    .subscribe(factorySubject::onNext, Throwable::printStackTrace)
        }

        var message: ByteArray
        val socket = DatagramSocket()

        message = "Wubba Lubba Dub Dub".toByteArray()

        socket.send(
                DatagramPacket(
                        message, message.size,
                        InetAddress.getByName("localhost"), manager.connectionPort
                )
        )

        println("send first")

        message = objectMapper.writeValueAsBytes(
                ClickerMessage(
                        Header.OK, testPort,
                        mutableMapOf(
                                "sessionId" to testSessionId,
                                "maxPage" to testMaxPage
                        )
                )
        )
        socket.send(
                DatagramPacket(
                        message, message.size,
                        InetAddress.getByName("localhost"), manager.connectionPort
                )
        )

        println("send second")

        with(factorySubject.blockingFirst() as LanDesktopControllerFactory) {
            assert(testMaxPage.toInt() == maxPage)
            assert(sessionId == testSessionId)
        }

        socket.close()
        println("Complete")
    }
}