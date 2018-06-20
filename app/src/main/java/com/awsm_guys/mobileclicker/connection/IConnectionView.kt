package com.awsm_guys.mobileclicker.connection

interface IConnectionView {
    fun showAskNameWindow()

    fun showSearch()

    fun showError(message: String)

    fun showSuccess()
}