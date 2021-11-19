package com.onyx.zhdanov.game.sudoku

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.view.SurfaceView
import com.onyx.android.sdk.pen.RawInputCallback
import com.onyx.android.sdk.pen.data.TouchPoint
import com.onyx.android.sdk.pen.data.TouchPointList
import com.onyx.android.sdk.rx.RxCallback
import com.onyx.android.sdk.rx.RxManager

class PenHandler(
    width: Int,
    height: Int,
    private val context: Context,
    private val surfaceView: SurfaceView,
    private val field: Field
): RawInputCallback() {
    private val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    private val canvas: Canvas = Canvas(bitmap)
    private val rxManager = RxManager.Builder.sharedSingleThreadManager()

    override fun onBeginRawDrawing(p0: Boolean, p1: TouchPoint) {
    }

    override fun onEndRawDrawing(p0: Boolean, p1: TouchPoint) {
    }

    override fun onRawDrawingTouchPointMoveReceived(p0: TouchPoint) {
    }

    override fun onRawDrawingTouchPointListReceived(points: TouchPointList) {
        drawScribbleToBitmap(points.points)
    }

    override fun onBeginRawErasing(p0: Boolean, p1: TouchPoint) {
    }

    override fun onEndRawErasing(p0: Boolean, p1: TouchPoint) {
    }

    override fun onRawErasingTouchPointMoveReceived(p0: TouchPoint) {
    }

    override fun onRawErasingTouchPointListReceived(p0: TouchPointList) {
    }

    override fun onPenUpRefresh(refreshRect: RectF) {
        val partialRefreshRequest = PartialRefreshRequest(
            context = context,
            surfaceView = surfaceView,
            bitmap = bitmap,
            fieldBitmap = field.bitmap,
            refreshRect = refreshRect
        )

        rxManager.enqueue(
            partialRefreshRequest,
            object : RxCallback<PartialRefreshRequest>() {
                override fun onNext(partialRefreshRequest: PartialRefreshRequest) {}
            }
        )
    }

    private fun drawScribbleToBitmap(list: List<TouchPoint>) {
        val path = Path()
        val prePoint = PointF(list[0].x, list[0].y)
        path.moveTo(prePoint.x, prePoint.y)
        for (point in list) {
            path.quadTo(prePoint.x, prePoint.y, point.x, point.y)
            prePoint.x = point.x
            prePoint.y = point.y
        }
        canvas.drawPath(path, paint)
    }

    companion object {
        val paint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 3f
            style = Paint.Style.STROKE
            color = Color.BLACK
        }
    }
}