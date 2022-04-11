package com.ly.app.base

import android.content.Context
import android.content.pm.PackageManager
import androidx.startup.Initializer
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * BaseApplication
 * 曾经的浪漫少年，如今只想搞钱
 * @author francis
 * @date 2021/9/22
 */

class BaseApplication : Initializer<Boolean> {


    override fun create(context: Context): Boolean {
        applicationContext = context.applicationContext
        return true
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

    companion object {
        private lateinit var applicationContext: Context
        fun getContext() = applicationContext

        /**
         * 获取Manifest的metaData
         */
        fun getMetaInfo(key: String): String? =
            getContext().packageManager.getApplicationInfo(
                getContext().packageName,
                PackageManager.GET_META_DATA
            ).metaData.get(key)?.toString()
    }
}