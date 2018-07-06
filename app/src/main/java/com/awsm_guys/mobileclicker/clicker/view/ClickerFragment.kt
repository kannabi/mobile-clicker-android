package com.awsm_guys.mobileclicker.clicker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.awsm_guys.mobileclicker.App
import com.awsm_guys.mobileclicker.R
import com.awsm_guys.mobileclicker.clicker.IClickerPresenter
import com.awsm_guys.mobileclicker.clicker.IClickerView
import com.awsm_guys.mobileclicker.di.ClickerComponent
import com.awsm_guys.mobileclicker.utils.LoggingMixin
import com.awsm_guys.mobileclicker.utils.wrapClickListening
import com.kannabi.simplelifecycleapilibrary.lifecycleapi.fragment.PresenterFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.mobile_clicker_layout.*

class ClickerFragment:
        PresenterFragment<IClickerView, ClickerComponent, IClickerPresenter>(),
        IClickerView, LoggingMixin {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mobile_clicker_layout, container, false)
    }

    override fun provideComponent(): ClickerComponent =
            (activity!!.application as App).componentProvider.getMobileClickerComponent()

    override fun onStart() {
        super.onStart()

        wrapClickListening(compositeDisposable, next_page_button) { getPresenter().onNextClick() }
        wrapClickListening(compositeDisposable, previous_page_button) {
            getPresenter().onPreviousClick()
        }
    }

    override fun showConnectionProcess() {
    }

    override fun showConnectionEstablished() {
    }

    override fun showConnectionLossDialog() {
        getPresenter().onGoToConnection()
    }

    override fun updateCurrentPage(number: Int) {
        current_page.text = number.toString()
    }

    override fun updateMaxPage(maxNumber: Int) {
        max_page.text = maxNumber.toString()
    }

    override fun showConnectionClose() {
    }
}