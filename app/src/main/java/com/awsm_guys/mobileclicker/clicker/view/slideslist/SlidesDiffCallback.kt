package com.awsm_guys.mobileclicker.clicker.view.slideslist

import android.support.v7.util.DiffUtil
import com.awsm_guys.mobileclicker.clicker.model.controller.poko.Page

class SlidesDiffCallback(
        private val oldList: List<Page>, private val newList: List<Page>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldItemPosition, newItemPosition)
    }
}