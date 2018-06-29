package com.awsm_guys.mobileclicker.clicker.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.awsm_guys.mobileclicker.App
import com.awsm_guys.mobileclicker.R
import com.awsm_guys.mobileclicker.clicker.IClickerPresenter
import com.awsm_guys.mobileclicker.clicker.IClickerView
import com.awsm_guys.mobileclicker.di.ClickerComponent
import com.awsm_guys.mobileclicker.utils.LoggingMixin
import com.kannabi.simplelifecycleapilibrary.lifecycleapi.fragment.PresenterFragment

class ClickerFragment:
        PresenterFragment<IClickerView, ClickerComponent, IClickerPresenter>(),
        IClickerView, LoggingMixin {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mobile_clicker_layout, container, false)
    }

    override fun provideComponent(): ClickerComponent =
            (activity!!.application as App).componentProvider.getMobileClickerComponent()


    override fun showConnectionProcess() {
    }

    override fun showConnectionEstablished() {
    }

    override fun showConnectionLossDialog() {
        getPresenter().onGoToConnection()
    }

    override fun updateCurrentPage(number: Int) {
    }

    override fun updateMaxPage(maxNumber: Int) {
    }

    override fun showConnectionClose() {
    }
}