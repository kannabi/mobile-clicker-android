package com.awsm_guys.mobileclicker.clicker.view.slideslist

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by kannabi on 15.03.2017.
 */

abstract class AbstractAdapter<K, T : RecyclerView.ViewHolder>(protected var mContext: Context) :
                                                                        RecyclerView.Adapter<T>() {

    protected val mItems = mutableListOf<K>()

    protected val mItemClickedSubject: PublishSubject<ItemClicked>
                                        by lazy { PublishSubject.create<ItemClicked>() }

    fun getItemClickedObservable(): Observable<ItemClicked> = mItemClickedSubject.hide()

    fun getItems() = mItems

    fun swapItems(items: MutableList<K>) {
        val diffCallback = getDiffUtil(mItems, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mItems.clear()
        mItems.addAll(items)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    fun addItems(items: MutableList<K>){
        mItems.addAll(ArrayList(items))
        notifyDataSetChanged()
    }

    fun deleteItem(item: K) {
        val index = mItems.indexOf(item)
        mItems.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int = mItems.size

    abstract fun updateItem(item: K)

    fun clear(){
        mItems.clear()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    protected abstract fun getDiffUtil(oldList: List<K>, newList: List<K>): DiffUtil.Callback

    inner class ItemClicked(val view: View?, val position: Int?, val item: K)
}
