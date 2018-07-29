package com.awsm_guys.mobileclicker.clicker.view.slideslist

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.util.DiffUtil
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import com.awsm_guys.mobileclicker.R
import com.awsm_guys.mobileclicker.clicker.model.controller.poko.Page

class SlidesAdapter(context: Context): AbstractAdapter<Page, SlideViewHolder>(context) {

    override fun updateItem(item: Page) { /*empty*/ }

    override fun getDiffUtil(oldList: List<Page>, newList: List<Page>): DiffUtil.Callback =
            SlidesDiffCallback(oldList, newList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder =
        SlideViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.slide_item, parent, false)
        )

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        val item = mItems[position]

        item.smallImageBase64.takeIf { it.isNotBlank() }.let {
            val decodedString =
                    try {
                        Base64.decode(it, Base64.DEFAULT)
                    } catch (e: Exception) {
                        Base64.decode(
                                mContext.resources.getString(R.string.small_image_placeholder),
                                Base64.DEFAULT
                        )
                    }
            holder.image?.setImageBitmap(
                            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            )
        }
        holder.title?.text = item.title
    }
}