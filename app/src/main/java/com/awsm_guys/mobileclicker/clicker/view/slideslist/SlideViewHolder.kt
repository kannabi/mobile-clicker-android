package com.awsm_guys.mobileclicker.clicker.view.slideslist

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.awsm_guys.mobileclicker.R

class SlideViewHolder(
        itemView: View?,
        val title: TextView? = itemView?.findViewById(R.id.title),
        val image: ImageView? = itemView?.findViewById(R.id.image)
) : ViewHolder(itemView)