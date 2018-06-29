package com.awsm_guys.mobileclicker.clicker.model.controller

import io.reactivex.Observable

interface DesktopController {
    fun init()

    fun getPageNumbers(): Int

    fun getCurrentPage(): Int

    fun switchPage(page: Int)

    /**
     * get a observable that emmit page number updates
     *
     * @return a new page number; negative number when session has been finished
     * */
    fun getPageSwitchingObservable(): Observable<Int>

    fun disconnect()
}