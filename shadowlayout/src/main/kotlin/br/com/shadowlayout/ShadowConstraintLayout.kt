package br.com.shadowlayout

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import br.com.shadowlayout.drawable.ShadowDrawable


class ShadowConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val path = Path()
    private val paint = Paint()
    private val shadowDrawable = ShadowDrawable()

    init {
        setWillNotDraw(false)
        shadowDrawable.callback = this
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === shadowDrawable
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
//        shadowDrawable.setBounds(0, 0, width, height)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        // call block() here if you want to draw behind children
        super.dispatchDraw(canvas)
        // call block() here if you want to draw over children

        children.forEach { view ->
            canvas?.let {
                shadowDrawable.draw(it)
            }
        }
    }

    private fun animationBackground(view: View, canvas: Canvas?) {
        val animator =
            ValueAnimator.ofFloat(canvas!!.width.toFloat(), 0f, canvas.width.toFloat())

        with(animator) {
            duration = 1000
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {}
        }
    }

    private fun updateShader(width: Float, factor: Float = -1F) {
        val left = width * factor
        val shader = LinearGradient(
            left, 0F, left + width, 0F,
            intArrayOf(
                Color.BLACK,
                Color.GRAY,
                Color.BLACK
            ),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        paint.shader = shader

    }
}