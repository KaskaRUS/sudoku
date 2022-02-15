package com.onyx.zhdanov.game.sudoku

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.PixelCopy
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.onyx.android.sdk.data.PenConstant
import com.onyx.android.sdk.pen.TouchHelper
import com.onyx.zhdanov.game.sudoku.models.Field
import com.onyx.zhdanov.game.sudoku.models.Grid
import com.onyx.zhdanov.game.sudoku.utils.drawRendererContent

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private val recognizeHandler = RecognizeHandler(context)

    lateinit var grid: Grid
    lateinit var touchHelper: TouchHelper
    lateinit var field: Field

    var onReady: ((penHandler: PenHandler, touchHelper: TouchHelper, field: Field) -> Unit)? = null
    var onRender: ((canvas: Canvas) -> Unit)? = null

    init {
        holder.addCallback(this@GameView)
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        val (penHandler, limit) = init()

        touchHelper = TouchHelper.create(
            this,
            true,
            penHandler
        )

        penHandler.touchHelper = touchHelper

        touchHelper.setLimitRect(limit, ArrayList<Rect>())
            .setStrokeWidth(10f)
            .openRawDrawing()

        touchHelper.setRawDrawingEnabled(false)

        touchHelper.setPenUpRefreshTimeMs(PenConstant.MIN_PEN_UP_REFRESH_TIME_MS)
        touchHelper.setPenUpRefreshEnabled(true)
        render()
        touchHelper.setRawDrawingEnabled(true)

        onReady?.let { it(penHandler, touchHelper, field) }
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        val (penHandler, limit) = init()
        penHandler.touchHelper = touchHelper

        touchHelper.bindHostView(this, penHandler)
            .setLimitRect(limit, ArrayList<Rect>())
            .setRawDrawingRenderEnabled(false)

        render()
        touchHelper
            .setRawDrawingRenderEnabled(true)
            .setRawDrawingEnabled(true)
    }

    private fun init(): Pair<PenHandler, Rect> {
        val limit = Rect()
        this.getLocalVisibleRect(limit)

        field = Field(width = limit.width(), height = limit.height(), grid)

        val penHandler = PenHandler(
            width = limit.width(),
            height = limit.height(),
            context = context,
            surfaceView = this,
            field = field,
            recognizeHandler = recognizeHandler
        )
        return Pair(penHandler, limit)
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        touchHelper
            .setRawDrawingRenderEnabled(false)
            .setRawDrawingEnabled(false)
            .closeRawDrawing()
    }

    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        PixelCopy.request(this, bitmap, {}, handler);
        return bitmap
    }

    fun render() {
        val canvas = this.holder.lockCanvas()
        canvas.drawColor(Color.WHITE)
        onRender?.let { it(canvas) }
            ?: run {
                field.draw()
                drawRendererContent(field.bitmap, canvas)
            }
        this.holder.unlockCanvasAndPost(canvas)
    }
}