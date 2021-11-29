package com.onyx.zhdanov.game.sudoku

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.onyx.android.sdk.data.PenConstant
import com.onyx.android.sdk.pen.TouchHelper
import com.onyx.zhdanov.game.sudoku.utils.drawRendererContent

class MySurfaceView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private val recognizeHandler = RecognizeHandler(context)

    init {
        Log.i("surface", "created")
        holder.addCallback(this@MySurfaceView)
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        Log.i("surface", "holder created")
        val limit = Rect()
        this.getLocalVisibleRect(limit)

        val field = Field(width = limit.width(), height = limit.height())

        val touchHelper = TouchHelper.create(
            this,
            PenHandler(
                width = limit.width(),
                height = limit.height(),
                context = context,
                surfaceView = this,
                field = field,
                recognizeHandler = recognizeHandler
            )
        )
        touchHelper.setLimitRect(limit, ArrayList<Rect>())
            .setStrokeWidth(3f)
            .openRawDrawing()

        touchHelper.setRawDrawingEnabled(false)

        touchHelper.setPenUpRefreshTimeMs(PenConstant.MIN_PEN_UP_REFRESH_TIME_MS)
        touchHelper.setPenUpRefreshEnabled(true)
        cleanSurfaceView(field)
        touchHelper.setRawDrawingEnabled(true)
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {

    }

    private fun cleanSurfaceView(field: Field) {
        val canvas = this.holder.lockCanvas()
        canvas.drawColor(Color.WHITE)
        field.draw()
        drawRendererContent(field.bitmap, canvas)
        this.holder.unlockCanvasAndPost(canvas)
    }
}