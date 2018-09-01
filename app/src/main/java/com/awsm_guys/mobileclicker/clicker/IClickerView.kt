package com.awsm_guys.mobileclicker.clicker

import com.awsm_guys.mobileclicker.clicker.model.controller.poko.Page

interface IClickerView {

    fun showConnectionProcess()

    fun showConnectionEstablished()

    fun showConnectionLossDialog()

    fun showConnectionClose()

    fun updateCurrentPage(number: Int)

    fun updateMaxPage(maxNumber: Int)

    fun updateSlidesImages(slides: List<Page>)
}