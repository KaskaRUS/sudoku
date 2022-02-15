package com.onyx.zhdanov.game.sudoku

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class RecognizeHandler(context: Context) {
    private val interpreter: Interpreter
    private val inputImageWidth: Int
    private val inputImageHeight: Int
    private val modelInputSize: Int

    init {
        val assetManager = context.assets
        val model = loadModelFile(assetManager, "mnist.tflite")
        interpreter = Interpreter(model)

        val inputShape = interpreter.getInputTensor(0).shape()
        inputImageWidth = inputShape[1]
        inputImageHeight = inputShape[2]
        modelInputSize = FLOAT_TYPE_SIZE * inputImageWidth * inputImageHeight * PIXEL_SIZE
    }

    fun recognize(bitmap: Bitmap): Int {
        val resizedImage = Bitmap.createScaledBitmap(
            bitmap,
            inputImageWidth,
            inputImageHeight,
            true
        )
        val byteBuffer = convertBitmapToByteBuffer(resizedImage)

        val output = Array(1) { FloatArray(OUTPUT_CLASSES_COUNT) }

        interpreter.run(byteBuffer, output)
        val result = output[0]

        Log.i("recognize", "recognized vector ${result.joinToString()}")

        val maxIndex = result.indices
            .filter { result[it] > MIN_RECOGNIZE_LEVEL }
            .maxByOrNull { result[it] }
            ?:- 0

        return maxIndex
    }

    private fun loadModelFile(assetManager: AssetManager, filename: String): ByteBuffer {
        val fileDescriptor = assetManager.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputImageWidth * inputImageHeight)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixelValue in pixels) {
            val normalizedPixelValue = normalizePixelColor(pixelValue)
            byteBuffer.putFloat(normalizedPixelValue)
        }

        return byteBuffer
    }

    private fun normalizePixelColor(pixelValue: Int): Float {
        val r = (pixelValue shr 16 and 0xFF)
        val g = (pixelValue shr 8 and 0xFF)
        val b = (pixelValue and 0xFF)

        return 1 - ((r + g + b) / 3.0f / 255.0f)
    }

    companion object {
        private const val FLOAT_TYPE_SIZE = 4
        private const val PIXEL_SIZE = 1
        private const val OUTPUT_CLASSES_COUNT = 10
        private const val MIN_RECOGNIZE_LEVEL = 0.5f
    }
}