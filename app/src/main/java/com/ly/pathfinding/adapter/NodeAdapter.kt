package com.ly.pathfinding.adapter

import android.util.Log
import com.ly.app.base.SimpleListAdapter
import com.ly.pathfinding.bean.Node
import com.ly.pathfinding.bean.NodeType
import com.ly.pathfinding.databinding.NodeLayoutBinding


/**
 * NodeAdapter
 *
 * @author francis
 * @date 2022/4/9
 */

class NodeAdapter : SimpleListAdapter<NodeLayoutBinding, Node>() {
    override fun setViewData(binding: NodeLayoutBinding, position: Int, value: Node) {
        binding.data = value
//        binding.root.setOnClickListener {
//            Log.d("", "setViewData: ${value.type}")
//            if (value.type != NodeType.FINISH && value.type != NodeType.USER) {
//                value.type = NodeType.WALL
//                notifyItemChanged(position)
//            }
//        }
        setItemClick(binding.root,position,value)
    }
}