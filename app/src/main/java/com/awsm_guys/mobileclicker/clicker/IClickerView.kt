package com.awsm_guys.mobileclicker.clicker

interface IClickerView {

    fun showConnectionProcess()

    fun showConnectionEstablished()

    fun showConnectionLossDialog()

    fun showConnectionClose()

    fun updateCurrentPage(number: Int)

    fun updateMaxPage(maxNumber: Int)
}