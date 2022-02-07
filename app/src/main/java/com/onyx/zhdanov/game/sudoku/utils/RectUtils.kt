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

fun RectF.intersection(rectF: RectF) =
    RectF(
        max(this.left, rectF.left),
        max(this.top, rectF.top),
        min(this.right, rectF.right),
        min(this.bottom, rectF.bottom),
    )

// to rect
fun RectF.normalize() =
    if (this.height() > this.width()) {
        val dt = (this.height() - this.width()) / 2
        RectF(
            this.left - dt,
            this.top,
            this.right + dt,
            this.bottom
        )
    } else {
        val dt = (this.width() - this.height()) / 2

        RectF(
            this.left,
            this.top - dt,
            this.right,
            this.bottom + dt
        )
    }

fun RectF.extend(value: Float) =
    RectF(
        this.left - value,
        this.top - value,
        this.right + value,
        this.bottom + value,
    )