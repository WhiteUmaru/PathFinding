package com.ly.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ly.app.base.ktx.getViewBinding

/**
 * BaseFragment
 *
 * @author francis
 * @date 2021/9/22
 */

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    lateinit var mBinding: VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = getViewBinding(inflater, container, false)
        return mBinding.root
    }
}