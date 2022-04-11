package com.ly.app.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.ly.app.base.ktx.getViewBinding
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * BaseActivity
 *
 * @author francis
 * @date 2021/9/22
 */

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {

    val mBinding: VB by lazy(mode = LazyThreadSafetyMode.NONE) {
        getViewBinding(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QMUIStatusBarHelper.translucent(this)
        QMUIStatusBarHelper.setStatusBarDarkMode(this)
        setContentView(mBinding.root)
        onCreateView(savedInstanceState)
    }

    abstract fun onCreateView(bundle: Bundle?)
}
