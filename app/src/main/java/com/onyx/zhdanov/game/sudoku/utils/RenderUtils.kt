package com.onyx.zhdanov.game.sudoku.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build

fun drawRendererContent(bitmap: Bitmap, canvas: Canvas) {
    val rect = Rect(0, 0, bitmap.width, bitmap.height)
    canvas.drawBitmap(bitmap, rect, rect, null)
}

fun drawShadowWithoutRect(canvas: Canvas, rect: RectF) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        canvas.clipOutRect(rect)
    }
    drawShadow(canvas)
}

fun drawShadow(canvas: Canvas) {
    canvas.drawColor(Color.GRAY, PorterDuff.Mode.DARKEN)
}