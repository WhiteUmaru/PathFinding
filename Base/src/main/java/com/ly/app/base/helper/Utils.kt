package com.ly.app.base.helper

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utils
 * 曾经的浪漫少年，如今只想搞钱
 * @author francis
 * @date 2021/9/28
 */

object Utils {
    const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    fun dateFormat(date: Date, format: String = DATE_FORMAT): String {
        //处理数据为0时的显示
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }

    fun stringToDate(date: String): Date {
        return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(date) ?: Date()
    }
}