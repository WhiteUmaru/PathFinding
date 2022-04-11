package com.ly.pathfinding.bean

/**
 * Node
 *
 * @author francis
 * @date 2022/4/9
 */

data class Node(
    // 坐标
    val x: Int,
    val y: Int,
    val position: Int,
    // 类型
    var type: NodeType = NodeType.NONE
) {
    // 代价
    var cost = 0
    var usedCost = 0

    // 前一个
    var previous: Node? = null

    fun getKey() = "$x,$y"
    fun cantWalk() = !type.canWalk()
    fun canThink() = type == NodeType.NONE || type == NodeType.FINISH

    @JvmName("setType1")
    fun setType(type: NodeType) {
        if (this.type == NodeType.START || this.type == NodeType.FINISH)
            return
        this.type = type
    }

    override fun toString(): String {
        return "($x,$y)=${type.name}"
    }

}