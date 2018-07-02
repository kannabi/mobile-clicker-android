package com.awsm_guys.mobileclicker.clicker

import com.kannabi.simplelifecycleapilibrary.lifecycleapi.Presenter

interface IClickerPresenter: Presenter<IClickerView> {
    fun onPageSwitching(number: Int)

    fun onNextClick()

    fun onPreviousClick()

    fun onGoToConnection()
}