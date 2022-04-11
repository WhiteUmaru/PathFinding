package com.ly.app.base

import androidx.databinding.ViewDataBinding

/**
 * BaseBinding
 *
 * @author francis
 * @date 2021/9/22
 */

interface BaseBinding<VB : ViewDataBinding> {
    fun VB.initBinding()
}