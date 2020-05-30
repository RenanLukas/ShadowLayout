package br.com.shadowlayout.drawable

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable

class ShadowDrawable : Drawable() {
    val matrix = Matrix()
    private val path = Path()
    private val paint = Paint().apply {
        //        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    val animation = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 1000L
        startDelay = 100L
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            invalidateSelf()
        }
    }

    private fun updateShader(width: Float, height: Float, factor: Float = -1F) {
        val left = width * factor
        val shader = LinearGradient(
            left, 0F, left + width, 0F,
            intArrayOf(
                Color.GREEN,
                Color.BLACK,
                Color.GREEN
            ),
            floatArrayOf(0.25f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
    }

    override fun draw(canvas: Canvas) {
        val valueAnimator = animation.animatedFraction
        val width = bounds.right.toFloat()
        val height = bounds.bottom.toFloat()

        matrix.reset()
        matrix.postTranslate((width*2)*valueAnimator, 0f)
        paint.shader.setLocalMatrix(matrix)

        canvas.drawPath(path, paint)
        if (!animation.isStarted) {
            animation.start()
        }
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
}