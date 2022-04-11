package com.ly.pathfinding.bean

import androidx.annotation.DrawableRes
import com.ly.pathfinding.R

/**
 * NodeType
 *
 * @author francis
 * @date 2022/4/9
 */

enum class NodeType(@DrawableRes val color: Int) {
    NONE(R.drawable.none),
    WALL(R.drawable.wall),
    PATH(R.drawable.path),
    THINK(R.drawable.think),
    THINKED(R.drawable.thinked),
    FINISH(R.drawable.finish),
    START(R.drawable.user),

    ;

    fun canWalk(): Boolean {
        return this == NONE || this == FINISH
    }


}