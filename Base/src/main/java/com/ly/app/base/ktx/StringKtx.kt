package com.ly.app.base.ktx

import com.google.gson.GsonBuilder

/**
 * ObjectKtx
 * 曾经的浪漫少年，如今只想搞钱
 * @author francis
 * @date 2021/9/22
 */

const val DATE_FORMAT = "yyyy-MM-dd HH:mm"

/**
 * 转换为对象
 */
inline fun <reified T> String.toObject(): T {
    return GsonBuilder()
        .setDateFormat(DATE_FORMAT)
        .serializeNulls()
        .create()
        .fromJson(this, T::class.java)
}

/**
 * 转换为json
 */
fun Any.toJson(): String {
    return GsonBuilder()
        .setDateFormat(DATE_FORMAT)
        .serializeNulls()
        .create()
        .toJson(this)
}