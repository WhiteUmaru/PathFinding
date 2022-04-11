package com.ly.pathfinding

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import com.ly.app.base.BaseActivity
import com.ly.pathfinding.adapter.NodeAdapter
import com.ly.pathfinding.bean.Node
import com.ly.pathfinding.bean.NodeType
import com.ly.pathfinding.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val adapter = NodeAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    override fun onCreateView(bundle: Bundle?) {
        mBinding.list.layoutManager = GridLayoutManager(this, 10, GridLayoutManager.HORIZONTAL, false)
        mBinding.list.adapter = adapter
        mBinding.clear.setOnClickListener { reSet() }
        mBinding.clearPath.setOnClickListener {
            adapter.getData().forEach {
                if (it.type == NodeType.THINKED || it.type == NodeType.THINK || it.type == NodeType.PATH) {
                    it.type = NodeType.NONE
                    it.cost = 0
                    adapter.notifyItemChanged(it.position)
                }
            }
        }
        mBinding.calculate.setOnClickListener {
            val startNode = adapter.getData().find { it.type == NodeType.START } ?: return@setOnClickListener
            val endNode = adapter.getData().find { it.type == NodeType.FINISH } ?: return@setOnClickListener
            PathFinding.calculate(startNode, endNode, adapter.getData()) {
                runOnUiThread {
                    if (it < 0) {
                        adapter.notifyDataSetChanged()
                    } else {
                        adapter.notifyItemChanged(it)
                    }
                }
            }
        }
        adapter.setOnItemClickListener { view: View, position: Int, action: Int, value: Node ->
            Log.d("", "setViewData: $value}")
            when (value.type) {
                NodeType.NONE -> {
                    value.type = NodeType.WALL
                }
                NodeType.WALL -> {
                    value.type = NodeType.NONE
                }
                NodeType.PATH -> {}
                NodeType.THINK -> {}
                NodeType.FINISH -> {}
                NodeType.START -> {}
            }
            adapter.notifyItemChanged(position)
        }
        reSet()
    }

    private fun reSet() {
        val data = mutableListOf<Node>()
        for (x in 1..20) {
            for (y in 1..10) {
                val node = Node(x, y, data.size)
                if (x == 2 && y == 5) node.type = NodeType.START
                if (x == 12 && y == 5) node.type = NodeType.FINISH
                data.add(node)
            }
        }
        adapter.setData(data)
    }

}