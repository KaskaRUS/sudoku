package com.onyx.zhdanov.game.sudoku.requests

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import com.onyx.android.sdk.rx.RxRequest
import com.onyx.android.sdk.utils.RectUtils
import com.onyx.zhdanov.game.sudoku.RecognizeHandler

class RecognizeRequest(
    private val bitmap: Bitmap,
    val rectf: RectF,
    private val recognizeHandler: RecognizeHandler
) : RxRequest() {

    var recognizedDigit: Int = 0

    override fun execute() {
        // copy rect of bitmap
        val rect = RectUtils.toRect(rectf)
        val copiedBitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(copiedBitmap)
        canvas.drawBitmap(bitmap, rect, RectF(0f, 0f, rectf.width(), rectf.height()), null)

        recognizedDigit = recognizeHandler.recognize(copiedBitmap)
    }
}