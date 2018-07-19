package com.awsm_guys.mobileclicker.clicker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.awsm_guys.mobileclicker.App
import com.awsm_guys.mobileclicker.R
import com.awsm_guys.mobileclicker.clicker.IClickerPresenter
import com.awsm_guys.mobileclicker.clicker.IClickerView
import com.awsm_guys.mobileclicker.clicker.model.controller.poko.Page
import com.awsm_guys.mobileclicker.clicker.view.slideslist.SlidesBottomListDialog
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

    private val slidesDialog by lazy {
        SlidesBottomListDialog(activity!!).apply {
            setContentView(LayoutInflater.from(activity!!).inflate(R.layout.slides_list, null))
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mobile_clicker_layout, container, false)
    }

    override fun provideComponent(): ClickerComponent =
            (activity!!.application as App).componentProvider.getMobileClickerComponent()

    override fun onStart() {
        super.onStart()

        wrapClickListening(compositeDisposable, current_page) { showSlidesList() }
        wrapClickListening(compositeDisposable, next_page_button) { getPresenter().onNextClick() }
        wrapClickListening(compositeDisposable, previous_page_button) {
            getPresenter().onPreviousClick()
        }
    }

    override fun showConnectionProcess() {
        enableElements(false)
    }

    override fun showConnectionEstablished() {
        Toast.makeText(context, "Connection established", Toast.LENGTH_SHORT).show()
        enableElements(true)
    }

    override fun showConnectionLossDialog() {
        getPresenter().onGoToConnection()
    }

    override fun updateCurrentPage(number: Int) {
        current_page.text = (number + 1).toString()
    }

    override fun updateMaxPage(maxNumber: Int) {
        max_page.text = (maxNumber).toString()
    }

    override fun showConnectionClose() {
        Toast.makeText(context, "Desktop disconnected", Toast.LENGTH_SHORT).show()
        enableElements(false)
    }

    private fun enableElements(enable: Boolean) {
        previous_page_button.isEnabled = enable
        next_page_button.isEnabled = enable
    }

    override fun updateSlidesImages(slides: List<Page>) {
        slidesDialog.updateSlides(slides.toMutableList())
    }

    private fun showSlidesList() = slidesDialog.show()
}