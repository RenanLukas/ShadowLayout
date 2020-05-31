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
import androidx.core.graphics.toRectF

class ShadowDrawable : Drawable() {
    var isResetDrawable: Boolean = false
        private set

    private val matrix = Matrix()
    private val path = Path()
    private val paint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
    }

    private val colorsGradient by lazy { intArrayOf(Color.GRAY, Color.LTGRAY, Color.GRAY) }
    private val positionsGradient by lazy { floatArrayOf(0f, 0.5f, 1f) }


    private val animationListener: ValueAnimator.AnimatorUpdateListener
            by lazy { ValueAnimator.AnimatorUpdateListener { invalidateSelf() } }

    private val animation: ValueAnimator by lazy {
        ValueAnimator.ofFloat(ANIMATION_FROM, ANIMATION_TO).apply {
            duration = DURATION
            startDelay = DELAY
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener(animationListener)
        }
    }

    private fun updateShader(width: Float, height: Float, factor: Float = -1F) {
        val left = width * factor
        val shader = LinearGradient(
            left, DEFAULT_VALUE, left + width,
            DEFAULT_VALUE,
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
        isResetDrawable = false
        animation.addUpdateListener(animationListener)
        if (!animation.isStarted) {
            animation.start()
        }
    }

    fun stopAnimation() {
        isResetDrawable = true
        paint.reset()
        animation.run {
            cancel()
            removeAllUpdateListeners()
            invalidateSelf()
        }
    }

    private fun draftShadowView(rectF: RectF) {
        path.addRect(rectF, Path.Direction.CW)
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        bounds?.let {
            val width = bounds.right.toFloat()
            val height = bounds.bottom.toFloat()
            draftShadowView(bounds.toRectF())
            updateShader(width = width, height = height)
        }
    }

    override fun getOpacity(): Int =
        PixelFormat.TRANSLUCENT

    companion object {
        const val DEFAULT_VALUE = 0f
        const val ANIMATION_FROM = DEFAULT_VALUE
        const val ANIMATION_TO = 1f
        const val DURATION = 1000L
        const val DELAY = 300L
    }

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(colorFilter: ColorFilter?) {}
}
