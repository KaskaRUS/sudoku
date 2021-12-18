package com.onyx.zhdanov.game.sudoku

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.os.Handler
import android.util.Log
import android.view.SurfaceView
import com.onyx.android.sdk.pen.RawInputCallback
import com.onyx.android.sdk.pen.TouchHelper
import com.onyx.android.sdk.pen.data.TouchPoint
import com.onyx.android.sdk.pen.data.TouchPointList
import com.onyx.android.sdk.rx.RxCallback
import com.onyx.android.sdk.rx.RxManager
import com.onyx.zhdanov.game.sudoku.requests.PartialRefreshRequest
import com.onyx.zhdanov.game.sudoku.requests.RecognizeRequest
import com.onyx.zhdanov.game.sudoku.utils.plus
import kotlin.math.max
import kotlin.math.min

class PenHandler(
    width: Int,
    height: Int,
    private val context: Context,
    private val surfaceView: SurfaceView,
    private val field: Field,
    private val recognizeHandler: RecognizeHandler,
    val onSuccess: (score: Long) -> Unit
) : RawInputCallback() {
    private val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    private val canvas: Canvas = Canvas(bitmap)
    private val rxManager = RxManager.Builder.sharedSingleThreadManager()
    private val drawRect: RectF = RectF(startRect)
    private val uiHandler: Handler = Handler()

    lateinit var touchHelper: TouchHelper

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
        Log.i("pen", "refresh rect: ${refreshRect}")

        Log.i("pen", "draw rect: ${drawRect}")
        Log.i("pen", "rect size: ${drawRect.width()} : ${drawRect.height()}")
        Log.i("pen", "rect center: ${drawRect.centerX()} : ${drawRect.centerY()}")

        val cellRect = field.getCellRect(drawRect)

        val recognizeRequest = RecognizeRequest(
            bitmap = bitmap,
            rectf = cellRect,
            recognizeHandler = recognizeHandler
        )

        drawRect.set(startRect)
        rxManager.enqueue(
            recognizeRequest,
            object : RxCallback<RecognizeRequest>() {
                override fun onNext(request: RecognizeRequest) {
                    Log.i("recognize", "response: ${request.recognizedDigit}")
                    Log.i("recognize", "recognize rect: ${request.rectf}")
                    val lastMistakeRects = field.getRectOfMistakes()
                    field.changeNumber(request.rectf.centerX(), request.rectf.centerY(), request.recognizedDigit)

                    val partialRefreshRequest = PartialRefreshRequest(
                        context = context,
                        surfaceView = surfaceView,
                        fieldBitmap = field.bitmap,
                        refreshRect = listOf(cellRect + refreshRect) + field.getRectOfMistakes() + lastMistakeRects,
                        touchHelper = touchHelper,
                    )

                    rxManager.enqueue(
                        partialRefreshRequest,
                        object : RxCallback<PartialRefreshRequest>() {
                            override fun onNext(partialRefreshRequest: PartialRefreshRequest) {
                                Log.i("draw", "refresh")
                                uiHandler.post {
                                    canvas.drawColor(Color.WHITE)
                                    if (field.isSuccess()) {
                                        onSuccess(0)

                                        touchHelper
                                            .setRawDrawingRenderEnabled(false)
                                            .setRawDrawingEnabled(false)
                                    }
                                }
                            }
                        }
                    )
                }
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

            drawRect.left = min(drawRect.left, point.x)
            drawRect.right = max(drawRect.right, point.x)
            drawRect.top = min(drawRect.top, point.y)
            drawRect.bottom = max(drawRect.bottom, point.y)
        }

        canvas.drawPath(path, paint)
    }

    companion object {
        val paint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 10f
            style = Paint.Style.STROKE
            color = Color.BLACK
        }

        val paintCleaner = Paint().apply {
            style = Paint.Style.FILL
            color = Color.WHITE
        }


        private val startRect = RectF(10000f, 10000f, 0f, 0f)
    }
}