package com.onyx.zhdanov.game.sudoku.requests

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.RectF
import android.graphics.Region
import android.util.Log
import android.view.SurfaceView
import com.onyx.android.sdk.api.device.EpdDeviceManager
import com.onyx.android.sdk.api.device.EpdDeviceManager.applyWithGCIntervalWithoutRegal
import com.onyx.android.sdk.api.device.epd.EpdController
import com.onyx.android.sdk.api.device.epd.UpdateMode
import com.onyx.android.sdk.pen.TouchHelper
import com.onyx.android.sdk.rx.RxRequest
import com.onyx.android.sdk.utils.RectUtils
import com.onyx.zhdanov.game.sudoku.utils.drawRendererContent
import com.onyx.zhdanov.game.sudoku.utils.plus

class PartialRefreshRequest(
    context: Context,
    private val surfaceView: SurfaceView,
    private val refreshRect: List<RectF>,
    private val fieldBitmap: Bitmap,
    private val touchHelper: TouchHelper
) : RxRequest() {

    @Throws(Exception::class)
    override fun execute() {
        renderToScreen(surfaceView)
    }

    private fun renderToScreen(surfaceView: SurfaceView) {
        touchHelper.setRawDrawingRenderEnabled(false)
        EpdController.setViewDefaultUpdateMode(surfaceView, UpdateMode.HAND_WRITING_REPAINT_MODE)
        val bounds = refreshRect.reduce { acc, rectF -> acc + rectF }
        val renderRect = RectUtils.toRect(bounds)
        val canvas = surfaceView.holder.lockCanvas(renderRect)
        try {
            canvas.clipRect(bounds)
            canvas.drawColor(Color.WHITE)
            drawRendererContent(fieldBitmap, canvas)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            surfaceView.holder.unlockCanvasAndPost(canvas)
            EpdController.resetViewUpdateMode(surfaceView)
            touchHelper.setRawDrawingRenderEnabled(true)
        }

    }

    init {
        setContext(context)
    }
}