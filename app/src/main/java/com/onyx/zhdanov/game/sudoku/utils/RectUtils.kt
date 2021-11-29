package com.onyx.zhdanov.game.sudoku.utils

import android.graphics.RectF
import kotlin.math.max
import kotlin.math.min

operator fun RectF.plus(rectF: RectF) =
    RectF(
        min(this.left, rectF.left),
        min(this.top, rectF.top),
        max(this.right, rectF.right),
        max(this.bottom, rectF.bottom),
    )