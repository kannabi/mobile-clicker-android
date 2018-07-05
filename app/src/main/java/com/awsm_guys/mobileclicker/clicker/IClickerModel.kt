package com.awsm_guys.mobileclicker.clicker

import com.awsm_guys.mobileclicker.clicker.model.events.ClickerEvent
import io.reactivex.Observable

interface IClickerModel {
    fun connect(): Observable<ClickerEvent>

    fun disconnect()

    fun switchPage(number: Int)

    fun onNextClick()

    fun onPreviousClick()
}