package com.ly.app.base

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.regex.Pattern

/**
 * BaseRecyclerViewAdapter
 *
 * 曾经的浪漫少年，如今只想搞钱
 * @author lihuayun
 * @since 2020/3/17
 */

abstract class BaseListAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = Collections.synchronizedList(mutableListOf<T>())

    private var mItemClickListener: OnItemClickListener<T>? = null

    private var mItemLongClickListener: OnItemLongClickListener<T>? = null

    private var canSelect: Boolean = true

    // 选择模式监听
    private var mOnSelectFlagChangeListen: OnSelectFlagChangeListen? = null

    // 已选择的数据
    private val selectSet = mutableSetOf<T>()

    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    // 是否开始多选模式
    private var selectFlag = false

    //  高亮关键字
    private var highlightKey: Pattern? = null

    // 是否展示空白
    private var showEmptyView = true

    private val ITEM_TYPE_CONTENT = -198
    private val ITEM_TYPE_EMPTY = -196

    //判断当前item类型
    final override fun getItemViewType(position: Int): Int {
        if (isEmpty()) {
            return ITEM_TYPE_EMPTY
        }
        return getCustomItemViewType(position)
    }

    open fun getCustomItemViewType(position: Int): Int {
        return ITEM_TYPE_CONTENT
    }

    override fun getItemCount(): Int {
        if (isEmpty() && showEmptyView) {
            return 1
        }
        return list.size
    }

    /**
     * 判断是否是空的
     */
    fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    fun setShowEmptyView(show: Boolean) {
        this.showEmptyView = show
    }

    private fun getEmptyViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_no_data, parent, false)
        ) {}
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ITEM_TYPE_EMPTY && isEmpty() && showEmptyView) {
            getEmptyViewHolder(parent)
        } else {
            onCreateCustomViewHolder(parent, viewType)
        }
    }


    /**
     * 加载页面
     */
    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 如果有headerView 则直接处理
        if (holder is HeaderViewHolder) {
            return
        }

        // 如果是空 则直接返回
        if (isEmpty()) {
            return
        }
        val itemData = list[position]
        setViewHolder(holder, position, itemData)
//        if (holder is MixingAdapterHolder<*>) holder.setViewHolder(position, itemData)
    }

    /**
     * 设置点击事件
     */
    fun setOnItemClickListener(itemClickListener: OnItemClickListener<T>) {
        this.mItemClickListener = itemClickListener
    }

    /**
     * 设置点击事件
     */
    fun setOnItemLongClickListener(itemClickListener: OnItemLongClickListener<T>) {
        this.mItemLongClickListener = itemClickListener
    }

    fun setData(newData: MutableList<T>) {
        list.clear()
        addData(newData)
    }

    fun addData(newData: MutableList<T>) {
        list.addAll(newData)
        notifyDataChanged()
    }

    fun addData(newData: T) {
        list.add(newData)
        callItemInserted(list.size)
    }


    fun putData(newData: T) {
        list.add(newData)
        callItemInserted(list.size)
    }


    fun resetData(newData: MutableList<T>) {
        // 进行对比 不再全部刷新
        list.clear()
        list.addAll(newData)
        notifyDataChanged()
    }

    /**
     * 删除数据
     * @param data T
     */
    fun remove(data: T): Int {
        val index = list.indexOf(data)
        if (index in 0 until list.size) {
            list.removeAt(index)
            callItemRemoved(index)
        }
        return index
    }

    /**
     * 删除数据
     * @param data T
     */
    fun removeAt(index: Int) {
        if (index < 0 || index >= itemCount) {
            return
        }
        list.removeAt(index)
        callItemRemoved(index)
    }

    /**
     * 新增数据
     * @param data T
     */
    @Synchronized
    fun add(data: T, index: Int) {
        list.add(index.coerceAtLeast(0), data)
        callItemInserted(index.coerceAtLeast(0))
    }

    /**
     * 更新消息
     */
    open suspend fun update(data: T) = withContext(Dispatchers.Main) {
        val index = list.indexOf(data)
        if (index >= 0) {
            list.removeAt(index)
            list.add(index, data)
            notifyItemChanged(index)
        } else {
            list.add(0, data)
            callItemInserted(0)
        }
    }


    /**
     * 设置列表被点击事件
     * @param view View     被点击的view
     * @param position Int  被点击的下标
     * @param action Int    被点击的事件
     */
    fun setItemClick(view: View?, position: Int, data: T, action: Int = ACTION_ITEM) {

        view?.setOnClickListener {
            mItemClickListener?.onItemClick(it, position, action, data)
        }

        view?.setOnLongClickListener {
            mItemLongClickListener?.onItemLongClick(it, position, action, data)
            return@setOnLongClickListener true
        }
    }

    /**
     * 通知点击效果
     * @param position Int
     * @param action Int
     * @param data T
     */
    fun callItemClick(view: View, position: Int, action: Int, data: T) {
        mItemClickListener?.onItemClick(view, position, action, data)
    }


    /**
     * 设置选择变更监听
     * @param mOnSelectFlagChangeListen OnSelectFlagChangeListen
     */
    fun setOnSelectFlagChangeListen(mOnSelectFlagChangeListen: OnSelectFlagChangeListen) {
        this.mOnSelectFlagChangeListen = mOnSelectFlagChangeListen
    }

    /**
     * 开始选择
     */
    fun startSelect() {
        // 如果不支持选择 则不开始选择
        if (!canSelect) {
            return
        }
        selectFlag = true
        selectSet.clear()
        mOnSelectFlagChangeListen?.change(selectFlag)
        notifyDataChanged()
    }

    /**
     * 停止选择
     */
    fun stopSelect() {
        selectFlag = false
        mOnSelectFlagChangeListen?.change(selectFlag)
        notifyDataChanged()
    }

    /**
     * 获取选择状态
     * @return Boolean
     */
    fun getSelectStatus(): Boolean {
        return selectFlag
    }

    /**
     * 是否包含该选择的元素
     */
    fun containsSelect(data: T): Boolean {
        return selectSet.contains(data)
    }

    /**
     * 添加选择的元素
     * @return Boolean  是否添加成功
     */
    fun addSelect(value: T): Boolean {
        return selectSet.add(value)
    }

    /**
     * 移除选择的元素
     * @return Boolean  是否添加成功
     */
    fun removeSelect(value: T): Boolean {
        return selectSet.remove(value)
    }

    /**
     * 设置已选择的数据
     */
    fun setSelect(set: MutableSet<T>) {
        this.selectSet.clear()
        this.selectSet.addAll(set)
        notifyDataChanged()
    }


    /**
     * 获取已选择的数据
     */
    fun getSelectList(): MutableList<T> {
        return selectSet.toMutableList()
    }

    /**
     * 清空已选择的
     */
    fun clearClick() {
        handler.post {
            selectSet.clear()
            notifyDataChanged()
        }
    }

    /**
     * 全选
     */
    fun selectAll() {
        if (selectSet.size < list.size) {
            selectSet.clear()
            list.forEach {
                selectSet.add(it)
            }
        } else {
            selectSet.clear()
        }
        notifyDataChanged()
    }

    /**
     * 是否支持选择
     * @param canSelect Boolean
     */
    fun setCanSelect(canSelect: Boolean) {

    }

    /**
     * 设置高亮关键字
     */
    fun setHighlightKey(highlightKey: String) {
        if (highlightKey.isEmpty()) {
            this.highlightKey = null
            return
        }
        this.highlightKey = Pattern.compile("(?i)${Pattern.quote(highlightKey)}")
    }

    fun getItemValue(index: Int): T? {
        return list.getOrNull(index)
    }

    @SuppressLint("notifyDataSetChanged")
    private fun notifyDataChanged() {
        handler.post {
            notifyDataSetChanged()
        }
    }

    private fun callItemInserted(position: Int) {
        handler.post {
            notifyItemInserted(position)
        }
    }

    private fun callItemRemoved(position: Int) {
        handler.post {
            notifyItemRemoved(position)
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /**
     * 创建界面的回调
     * @param parent ViewGroup
     * @return RecyclerView.ViewHolder
     */
    abstract fun onCreateCustomViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * 界面与数据绑定的回调
     * @param holder ViewHolder
     * @param position Int
     */
    abstract fun setViewHolder(holder: RecyclerView.ViewHolder, position: Int, value: T)

    /**
     * 判断数据是否相同 比较
     */
    abstract fun compare(defValue: T, value: T): Boolean

    /**
     * 获取全部数据
     * @return MutableList<T>
     */
    fun getData(): MutableList<T> {
        return list
    }


    /**
     * 点击监听回调
     * @param T
     */
    fun interface OnItemClickListener<T> {
        fun onItemClick(view: View, position: Int, action: Int, data: T)
    }

    /**
     * 点击监听回调
     * @param T
     */
    fun interface OnItemLongClickListener<T> {
        fun onItemLongClick(view: View, position: Int, action: Int, data: T)
    }

    /**
     *  选择模式切换监听
     */
    fun interface OnSelectFlagChangeListen {
        fun change(start: Boolean)
    }

    /**
     * 加载更多
     */
    fun interface OnNeedMoreData {
        fun loading(start: Boolean)
    }

    companion object {
        const val ACTION_ITEM = 1
    }

}