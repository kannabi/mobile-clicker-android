package com.awsm_guys.mobileclicker.clicker.model.controller

import io.reactivex.Observable

interface DesktopController {
    fun init(): Observable<DesktopController>

    fun getPageNumbers(): Int

    fun getCurrentPage(): Int

    fun switchPage(page: Int)

    fun getPageSwitchingObservable(): Observable<Int>

    fun disconnect()
}