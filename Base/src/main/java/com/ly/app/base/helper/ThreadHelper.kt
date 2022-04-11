package com.ly.app.base.helper

import android.annotation.SuppressLint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.concurrent.*

/**
 * TimerHelp  -- 异步操作
 *
 * @author francis
 * @date 2021/11/6
 */

object ThreadHelper {

    // 时间单位
    private val TIME_UNIT = TimeUnit.MILLISECONDS

    private val pool: ScheduledExecutorService = Executors.newScheduledThreadPool(5)

    @SuppressLint("UseSparseArrays")
    private val hashMap = HashMap<Int, ScheduledFuture<*>>()
    private val lockList = HashMap<Int, CountDownLatch>()

    val scope by lazy { MainScope() }

    // 取Id 加盐防止重复
    private fun getId(salt: Int = 0): Int {
        val id = hashMap.size + salt
        if (hashMap.containsKey(id))
            return getId(id + 1)
        return id
    }

    /**
     * 获取执行结果
     */
    private fun getFuture(id: Int): Future<*>? {
        if (!hashMap.containsKey(id)) {
            return null
        }
        return hashMap[id]
    }

    fun runMain(runnable: Runnable) {
        scope.launch(Dispatchers.Main) {
            runnable.run()
        }
    }

    /**
     * 延期执行任务
     */
    fun schedule(runnable: Runnable, delay: Long = 10): Int {
        val id = getId()
        hashMap[id] = pool.schedule(runnable, delay, TIME_UNIT)
        return id
    }

    /**
     * 查看任务是否活跃
     */
    fun isActive(id: Int): Boolean {
        return !isDeath(id)
    }

    /**
     * 是否停止了
     */
    fun isDeath(id: Int): Boolean {
        val future = getFuture(id) ?: return true
        return future.isCancelled || future.isDone
    }

    /**
     * 停止特定id的任务
     */
    fun cancel(id: Int): Boolean {
        val future = getFuture(id) ?: return false
        val flag = future.cancel(true)
        hashMap.remove(id)
        return flag
    }

    /**
     * 周期执行延迟任务
     */
    fun scheduleAtFixedRate(command: Runnable, initialDelay: Long, period: Long): Int {
        return scheduleAtFixedRate(command, initialDelay, period, getId())
    }

    /**
     * 周期执行固定Id延迟任务
     */
    fun scheduleAtFixedRate(
        command: Runnable,
        initialDelay: Long,
        period: Long,
        id: Int,
    ): Int {
        cancel(id)
        hashMap[id] = pool.scheduleAtFixedRate(command, initialDelay, period, TIME_UNIT)
        return id
    }

    fun getLock(count: Int): Int {
        val id = getId()
        lockList.put(id, CountDownLatch(count))
        return id
    }

    fun await(id: Int) {
        lockList.get(id)?.await()
    }

    fun countDown(id: Int) {
        lockList.get(id)?.countDown()
    }
}