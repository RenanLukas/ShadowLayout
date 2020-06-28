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
import kotlin.math.min

class ShadowLinearDrawable : Drawable() {
    var parentWidth: Int = 0

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
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener(animationListener)
        }
    }

    private fun updateShader(width: Float) {
        if (paint.shader != null) return
        val shader = LinearGradient(
            min(300F, width).unaryMinus(),
            DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE,
            colorsGradient,
            positionsGradient,
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
    }

    override fun draw(canvas: Canvas) {
        if (!animation.isRunning) return
        settingsMatrix()
        canvas.drawPath(path, paint)
    }

    private fun settingsMatrix() {
        val valueAnimator = animation.animatedFraction
        matrix.reset()
        matrix.postTranslate((parentWidth * 2) * valueAnimator, DEFAULT_VALUE)
        paint.shader.setLocalMatrix(matrix)
    }


    fun startAnimation() {
        animation.addUpdateListener(animationListener)
        if (!animation.isStarted) {
            animation.start()
        }
    }

    fun stopAnimation() {
        path.reset()
        animation.run {
            cancel()
            removeAllUpdateListeners()
            invalidateSelf()
        }
    }

    private fun draftShadowView(rectF: RectF) {
        if (animation.isRunning) return
        path.addRoundRect(
            rectF,
            floatArrayOf(
                10f, 10f, 10f, 10f,
                10f, 10f, 10f, 10f
            ), Path.Direction.CW
        )
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        bounds?.let {
            val width = bounds.right.toFloat()
            draftShadowView(bounds.toRectF())
            updateShader(width = width)
        }
    }

    override fun getOpacity(): Int =
        PixelFormat.TRANSLUCENT

    companion object {
        const val DURATION = 1500L
        const val DEFAULT_VALUE = 0f
        const val ANIMATION_FROM = DEFAULT_VALUE
        const val ANIMATION_TO = (DURATION + 500L).toFloat()
    }

    fun isRunning() = animation.isRunning

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(colorFilter: ColorFilter?) {}
}
