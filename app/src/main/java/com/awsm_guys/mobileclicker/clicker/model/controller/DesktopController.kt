package com.awsm_guys.mobileclicker.clicker.model.controller

import com.awsm_guys.mobileclicker.clicker.model.controller.poko.Meta
import io.reactivex.Observable

interface DesktopController {
    fun init()

    fun getPageNumbers(): Int

    fun getCurrentPage(): Int

    fun switchPage(page: Int)

    /**
     * get a observable that emmit page number updates
     *
     * @return a new page number
     * */
    fun getPageSwitchingObservable(): Observable<Int>

    fun getMetaUpdateObservable(): Observable<Meta>

    fun disconnect()

    fun isConnected(): Boolean
}