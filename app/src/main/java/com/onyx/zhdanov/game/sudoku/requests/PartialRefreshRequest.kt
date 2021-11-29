package com.onyx.zhdanov.game.sudoku.requests

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.RectF
import android.util.Log
import android.view.SurfaceView
import com.onyx.android.sdk.api.device.EpdDeviceManager
import com.onyx.android.sdk.api.device.EpdDeviceManager.applyWithGCIntervalWithoutRegal
import com.onyx.android.sdk.api.device.epd.EpdController
import com.onyx.android.sdk.api.device.epd.UpdateMode
import com.onyx.android.sdk.rx.RxRequest
import com.onyx.android.sdk.utils.RectUtils
import com.onyx.zhdanov.game.sudoku.utils.drawRendererContent

class PartialRefreshRequest(
    context: Context,
    private val surfaceView: SurfaceView,
    private val bitmap: Bitmap?,
    private val refreshRect: RectF,
    private val fieldBitmap: Bitmap
) : RxRequest() {

    @Throws(Exception::class)
    override fun execute() {
        renderToScreen(surfaceView, bitmap)
    }

    private fun renderToScreen(surfaceView: SurfaceView, bitmap: Bitmap?) {
        Log.i("PartialRefreshRequest", "${refreshRect}")
        val renderRect = RectUtils.toRect(refreshRect)
        EpdController.setViewDefaultUpdateMode(surfaceView, UpdateMode.HAND_WRITING_REPAINT_MODE)
        val canvas = surfaceView.holder.lockCanvas(renderRect)
        try {
            canvas.clipRect(renderRect)
            canvas.drawColor(Color.WHITE)
            drawRendererContent(fieldBitmap, canvas)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            surfaceView.holder.unlockCanvasAndPost(canvas)
            EpdController.resetViewUpdateMode(surfaceView)
        }
    }

    init {
        setContext(context)
    }
}