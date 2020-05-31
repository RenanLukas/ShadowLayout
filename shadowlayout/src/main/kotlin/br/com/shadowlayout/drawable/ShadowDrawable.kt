package br.com.shadowlayout.drawable

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable

class ShadowDrawable : Drawable() {
    private val matrix = Matrix()
    private val path = Path()
    private val paint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    private val colorsGradient by lazy { intArrayOf(Color.GRAY, Color.LTGRAY, Color.GRAY) }
    private val positionsGradient by lazy { floatArrayOf(0f, 0.5f, 1f) }


    private val animation = ValueAnimator.ofFloat(ANIMATION_FROM, ANIMATION_TO).apply {
        duration = DURATION
        startDelay = DELAY
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            invalidateSelf()
        }
    }

    private fun updateShader(width: Float, height: Float, factor: Float = -1F) {
        val left = width * factor
        val shader = LinearGradient(
            left, DEFAULT_VALUE, left + width, DEFAULT_VALUE,
            colorsGradient,
            positionsGradient,
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
    }

    override fun draw(canvas: Canvas) {
        settingsMatrix()
        canvas.drawPath(path, paint)
        startAnimation()
    }

    private fun settingsMatrix() {
        val valueAnimator = animation.animatedFraction
        val width = bounds.right.toFloat()

        matrix.reset()
        matrix.postTranslate((width * 2) * valueAnimator, DEFAULT_VALUE)
        paint.shader.setLocalMatrix(matrix)
    }


    fun startAnimation() {
        if (!animation.isStarted) {
            animation.start()
        }
    }

    fun stopAnimation(){
        animation.cancel()
        animation.removeAllUpdateListeners()
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        bounds?.let {
            val width = bounds.right.toFloat()
            val height = bounds.bottom.toFloat()
            //remove after
            path.addRect(
                RectF(
                    bounds.left.toFloat(),
                    bounds.top.toFloat(),
                    width,
                    height
                ),
                Path.Direction.CW
            )
            updateShader(width = width, height = height)
        }
    }

    override fun setAlpha(alpha: Int) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOpacity(): Int =
        PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        const val DEFAULT_VALUE = 0f
        const val ANIMATION_FROM = DEFAULT_VALUE
        const val ANIMATION_TO = 1f
        const val DURATION = 1000L
        const val DELAY = 300L
    }
}