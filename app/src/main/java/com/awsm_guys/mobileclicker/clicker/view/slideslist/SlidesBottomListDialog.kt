package com.awsm_guys.mobileclicker.clicker.view.slideslist

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.awsm_guys.mobileclicker.R
import com.awsm_guys.mobileclicker.clicker.model.controller.poko.Page
import io.reactivex.Observable

class SlidesBottomListDialog(
        context: Context
) : BottomSheetDialog(context) {

    private lateinit var adapter: AbstractAdapter<Page, SlideViewHolder>
    lateinit var clickObservable: Observable<AbstractAdapter<Page, SlideViewHolder>.ItemClicked>

    override fun setContentView(view: View) {
        super.setContentView(view)
        setupLayoutParams(view)
        setupRecycler()
    }

    private fun setupRecycler() {
        findViewById<RecyclerView>(R.id.slides_recycler)?.apply {
            this@apply.adapter = SlidesAdapter(context)
                    .also(this@SlidesBottomListDialog::adapter::set)
                    .also {
                        clickObservable =
                                this@SlidesBottomListDialog.adapter.getItemClickedObservable()
                    }
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun updateSlides(slides: MutableList<Page>) = adapter.swapItems(slides)

    private fun setupLayoutParams(view: View) {
        val params = view.layoutParams
        params.height = (context.resources.displayMetrics.heightPixels.toDouble() * 0.66).toInt()
        view.layoutParams = params
    }
}