package com.ly.pathfinding

import com.ly.app.base.helper.ThreadHelper
import com.ly.pathfinding.bean.Node
import com.ly.pathfinding.bean.NodeType
import kotlin.math.absoluteValue

/**
 * PathFinding
 *
 * @author francis
 * @date 2022/4/9
 */

object PathFinding {
    private const val TAG = "PathFinding"
    private val map = mutableMapOf<String, Node>()
    private var refresh: Refresh? = null

    fun calculate(startNode: Node, endNode: Node, data: MutableList<Node>, refresh: Refresh) {
        ThreadHelper.schedule({
            map.clear()
            this.refresh = refresh
            data.forEach {
                map.put(it.getKey(), it)
            }
            val queue = mutableListOf<Node>()
            queue.add(startNode)
            while (queue.isNotEmpty()) {
                queue.sortBy { it.cost }
                val root = queue.removeAt(0)
                if (root == endNode) {
                    var previous = root.previous
                    while (previous != null) {
                        previous.setType(NodeType.PATH)
                        refresh.refresh(previous.position)
                        Thread.sleep(100)
                        previous = previous.previous
                    }
                    return@schedule
                }
                root.setType(NodeType.THINKED)
                // 计算左右上下
                guessNode(root, getLeft(root), endNode, queue)
                guessNode(root, getRight(root), endNode, queue)
                guessNode(root, getTop(root), endNode, queue)
                guessNode(root, getBottom(root), endNode, queue)
                refresh.refresh(root.position)
                Thread.sleep(100)
            }
        })

    }

    private fun calculateCost(root: Node, endNode: Node): Int {
        // 勾股定理
//        return (
//                (root.x - endNode.x).toDouble().pow(2) +
//                        (root.y - endNode.y).toDouble().pow(2)
//                ).toInt()
        return (root.x - endNode.x).absoluteValue + (root.y - endNode.y).absoluteValue
    }

    private fun guessNode(root: Node, node: Node?, endNode: Node, queue: MutableList<Node>) {
        if (node != null && node.canThink()) {
            node.usedCost = root.usedCost + 1
            node.cost = node.usedCost + calculateCost(node, endNode)
            node.setType(NodeType.THINK)
            node.previous = root
            queue.add(node)
            this.refresh?.refresh(node.position)
        }
    }

    private fun getLeft(target: Node): Node? = map["${target.x - 1},${target.y}"]
    private fun getRight(target: Node): Node? = map["${target.x + 1},${target.y}"]
    private fun getTop(target: Node): Node? = map["${target.x},${target.y - 1}"]
    private fun getBottom(target: Node): Node? = map["${target.x},${target.y + 1}"]

    fun interface Refresh {
        fun refresh(position: Int)
    }
}