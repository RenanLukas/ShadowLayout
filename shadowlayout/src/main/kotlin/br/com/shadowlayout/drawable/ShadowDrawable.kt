package br.com.shadowlayout.drawable

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable

class ShadowDrawable : Drawable() {

    val aniamtion = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 1000
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            invalidateSelf()
        }
    }

    override fun draw(canvas: Canvas) {
        aniamtion.start()
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        val width = bounds?.width()
        val height = bounds?.height()
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