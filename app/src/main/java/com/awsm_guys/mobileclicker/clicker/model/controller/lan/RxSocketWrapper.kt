package com.awsm_guys.mobileclicker.clicker.model.controller.lan

import com.awsm_guys.mobileclicker.utils.LoggingMixin
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.io.Closeable
import java.net.Socket
import java.net.SocketException

class RxSocketWrapper(
        private val socket: Socket,
        private var bufferSize: Int = 2048
        ): LoggingMixin, Closeable {

    private val MESSAGE_END: ByteArray = byteArrayOf(-1)
    private val EMPTY_BYTE: Byte = -2

    private val compositeDisposable = CompositeDisposable()

    private val outputStream = socket.getOutputStream()
        get() = synchronized(lock) { return field }
    private val inputStream = socket.getInputStream()

    private val lock = Any()
    private val dataSubject = PublishSubject.create<String>()
        get() = synchronized(lock) { return field }

    val inputObservable: Observable<String> by lazy {

        compositeDisposable.add(
            Completable.fromCallable {
                try {
                    val data = ByteArray(bufferSize)
                    while (inputStream.read(data) != -1) {
                        processData(data)
                        data.fill(EMPTY_BYTE)
                    }
                } catch (e: SocketException) {
                    dataSubject.onComplete()
                } catch (e: Exception) {
                    trace(e)
                    dataSubject.onError(e)
                }
            }.subscribeOn(Schedulers.io())
            .subscribe(dataSubject::onComplete, dataSubject::onError)
        )

        return@lazy dataSubject.hide()
    }

    private val stringBuilder = StringBuilder()

    private fun processData(data: ByteArray) {
        var previousIndex = 0
        data.forEachIndexed { i, it ->
            when (it) {
                MESSAGE_END[0] -> {
                    stringBuilder.append(String(data, previousIndex, i - previousIndex))
                    dataSubject.onNext(stringBuilder.toString())
                    stringBuilder.setLength(0)
                    previousIndex = i + 1
                }
                EMPTY_BYTE -> previousIndex = i
            }
        }
        if (previousIndex != data.lastIndex) {
            stringBuilder.append(String(data, previousIndex, bufferSize - previousIndex))
        }
    }

    fun sendData(data: String) {
        compositeDisposable.add(
            Completable.fromCallable {
                outputStream.write(data.toByteArray())
                outputStream.write(MESSAGE_END)
                outputStream.flush()
            }.subscribeOn(Schedulers.io())
            .subscribe({ log("send $data") }, ::trace)
        )
    }

    fun sendData(data: String, completeCallback: () -> Unit) {
        compositeDisposable.add(
                Completable.fromCallable {
                    outputStream.write(data.toByteArray())
                    outputStream.write(MESSAGE_END)
                    outputStream.flush()
                }.subscribeOn(Schedulers.io())
                        .subscribe(completeCallback, ::trace)
        )
    }

    override fun close() {
        inputStream.close()
        outputStream.close()
        socket.close()
        compositeDisposable.clear()
    }
}