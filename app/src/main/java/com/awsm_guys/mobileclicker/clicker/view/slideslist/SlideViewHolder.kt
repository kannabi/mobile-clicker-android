package com.awsm_guys.mobileclicker.clicker.view.slideslist

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import com.awsm_guys.mobileclicker.R

class SlideViewHolder(
        itemView: View,
        val title: TextView? = itemView.findViewById(R.id.title),
        val image: WebView? = itemView.findViewById(R.id.image)
) : ViewHolder(itemView) {
    init {
        image?.settings?.apply {
            loadWithOverviewMode = true
            useWideViewPort = true
        }
    }
}