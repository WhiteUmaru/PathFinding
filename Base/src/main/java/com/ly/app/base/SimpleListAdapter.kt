package com.ly.app.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ly.app.base.ktx.getViewBinding

/**
 * BaseRecyclerViewAdapter
 *
 * 曾经的浪漫少年，如今只想搞钱
 * @author lihuayun
 * @since 2020/3/17
 */

abstract class SimpleListAdapter<VB : ViewDataBinding, T> : BaseListAdapter<T>() {

    override fun onCreateCustomViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SimpleViewHolder(getViewBinding<VB>(LayoutInflater.from(parent.context), parent, false))
    }

    override fun setViewHolder(holder: RecyclerView.ViewHolder, position: Int, value: T) {
        if (holder is SimpleViewHolder<*>) {
            setViewData(holder.binding as VB, position, value)
        }
        setItemClick(holder.itemView, position, value)
    }


    /**
     * 判断数据是否相同 比较
     */
    override fun compare(defValue: T, value: T): Boolean {
        return defValue == value
    }


    abstract fun setViewData(binding: VB, position: Int, value: T)

    class SimpleViewHolder<V : ViewDataBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root)

}