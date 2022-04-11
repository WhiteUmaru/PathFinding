package com.ly.pathfinding.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ly.app.base.helper.ResUtils
import com.ly.pathfinding.bean.NodeType

/**
 * ViewTools
 *
 * @author francis
 * @date 2022/4/9
 */

object ViewTools {

    @BindingAdapter("android:setBg")
    @JvmStatic
    fun setBg(view: TextView, type: NodeType) {
        view.background = ResUtils.getDrawable(type.color)
    }

    @BindingAdapter("android:setCost")
    @JvmStatic
    fun setCost(view: TextView, cost: Int) {
        view.text = if (cost == 0) "" else "$cost"
    }
}