package com.onyx.zhdanov.game.sudoku.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

fun drawRendererContent(bitmap: Bitmap, canvas: Canvas) {
    val rect = Rect(0, 0, bitmap.width, bitmap.height)
    canvas.drawBitmap(bitmap, rect, rect, null)
}