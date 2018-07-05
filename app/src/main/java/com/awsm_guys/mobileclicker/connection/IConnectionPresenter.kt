package com.awsm_guys.mobileclicker.connection

import com.kannabi.simplelifecycleapilibrary.lifecycleapi.Presenter

interface IConnectionPresenter: Presenter<IConnectionView> {
    fun onUsernameEnter(username: String)

    fun onCancelSearch()
}