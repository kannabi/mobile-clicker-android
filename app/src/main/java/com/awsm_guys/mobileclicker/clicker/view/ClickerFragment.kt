package com.awsm_guys.mobileclicker.clicker.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.awsm_guys.mobileclicker.App
import com.awsm_guys.mobileclicker.R
import com.awsm_guys.mobileclicker.clicker.IClickerPresenter
import com.awsm_guys.mobileclicker.clicker.IClickerView
import com.awsm_guys.mobileclicker.clicker.model.controller.poko.Page
import com.awsm_guys.mobileclicker.clicker.view.slideslist.AbstractAdapter
import com.awsm_guys.mobileclicker.clicker.view.slideslist.SlideViewHolder
import com.awsm_guys.mobileclicker.clicker.view.slideslist.SlidesBottomListDialog
import com.awsm_guys.mobileclicker.di.ClickerComponent
import com.awsm_guys.mobileclicker.utils.LoggingMixin
import com.awsm_guys.mobileclicker.utils.wrapClickListening
import com.kannabi.simplelifecycleapilibrary.lifecycleapi.fragment.PresenterFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.mobile_clicker_layout.*



class ClickerFragment:
        PresenterFragment<IClickerView, ClickerComponent, IClickerPresenter>(),
        IClickerView,
        LoggingMixin
{

    private val compositeDisposable = CompositeDisposable()

    private val slidesDialog by lazy {
        SlidesBottomListDialog(activity!!).apply {
            setContentView(LayoutInflater.from(activity!!).inflate(R.layout.slides_list, null))
            compositeDisposable.add(
                    clickObservable
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(AbstractAdapter<Page, SlideViewHolder>.ItemClicked::item)
                            .map(Page::title)
                            .map(String::toInt)
                            .map(Int::dec)
                            .subscribe(getPresenter()::onPageSwitching, ::trace)
            )
        }
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.mobile_clicker_layout, container, false)

    override fun provideComponent(): ClickerComponent =
            (activity!!.application as App).componentProvider.getMobileClickerComponent()

    override fun onStart() {
        super.onStart()

        wrapClickListening(compositeDisposable, current_page) { showSlidesList() }
        wrapClickListening(compositeDisposable, next_page_button) { getPresenter().onNextClick() }
        wrapClickListening(compositeDisposable, previous_page_button) {
            getPresenter().onPreviousClick()
        }

        compositeDisposable.add(
                (activity as VolumeButtonBroadcastDelegate.VolumeButtonsObservable)
                        .getObservable()
                        .observeOn(Schedulers.io())
                        .subscribe({
                            when (it) {
                                KeyEvent.KEYCODE_VOLUME_UP -> getPresenter().onNextClick()
                                KeyEvent.KEYCODE_VOLUME_DOWN -> getPresenter().onPreviousClick()
                            }
                        }, ::trace)
        )
    }

    override fun showConnectionProcess() {
        enableElements(false)
    }

    override fun showConnectionEstablished() {
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

    override fun onDestroy() {
        super.onDestroy()
        slidesDialog.dismiss()
    }
}