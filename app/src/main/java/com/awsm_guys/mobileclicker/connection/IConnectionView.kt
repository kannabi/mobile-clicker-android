package com.awsm_guys.mobileclicker.connection

interface IConnectionView {

    fun showAskNameWindow(currentName: String = "")

    fun showSearch()

    fun showError(message: String)

    fun showSuccess()
}