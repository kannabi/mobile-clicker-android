package com.awsm_guys.mobileclicker.clicker.view.slideslist

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import com.awsm_guys.mobileclicker.R
import com.awsm_guys.mobileclicker.clicker.model.controller.poko.Page
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class SlidesAdapter(context: Context): AbstractAdapter<Page, SlideViewHolder>(context) {

    companion object {
        const val HTML_START = """<!DOCTYPE html><html><head></head><body><img src="data:image/png;base64,"""
        const val HTML_END = """"  style="height: 100%; width: 100%; object-fit: contain" alt"It is supposed to be a mini-slide there but smth went wrong :|"></body></html>"""
    }

    private val imageClickSubject = PublishSubject.create<ItemClicked>()

    override fun updateItem(item: Page) { /*empty*/ }

    override fun getDiffUtil(oldList: List<Page>, newList: List<Page>): DiffUtil.Callback =
            SlidesDiffCallback(oldList, newList)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder =
        SlideViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.slide_item, parent, false)
        ).also { holder: SlideViewHolder ->
            holder.image?.setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val position = holder.adapterPosition
                    imageClickSubject.onNext(
                        ItemClicked(
                                holder.image,
                                position,
                                mItems[position]
                        )
                    )
                }
                return@setOnTouchListener view.performClick()

            }
        }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        val item = mItems[position]

        item.smallImageBase64.takeIf { it.isNotBlank() }.let {
            holder.image?.loadData("$HTML_START$it$HTML_END", "text/html", "UTF-8")
        }
        holder.title?.text = item.title
    }

    override fun getItemClickedObservable(): Observable<ItemClicked> =
            imageClickSubject.hide().observeOn(Schedulers.io())
}