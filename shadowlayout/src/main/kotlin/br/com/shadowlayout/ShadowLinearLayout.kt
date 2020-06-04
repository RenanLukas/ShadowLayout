package br.com.shadowlayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import br.com.shadowlayout.drawable.ShadowDrawable


class ShadowLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val shadowDrawable = ShadowDrawable()
    private val paint = Paint()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setWillNotDraw(false)
        shadowDrawable.callback = this
        setLayerType(View.LAYER_TYPE_HARDWARE, paint)
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === shadowDrawable
    }

    override fun dispatchDraw(canvas: Canvas?) {
        // call block() here if you want to draw behind children
        super.dispatchDraw(canvas)
        // call block() here if you want to draw over children
        if (shadowDrawable.isResetDrawable) return
        shadowDrawable.withParent = this.measuredWidth
        children.forEach { view ->
            canvas?.let {
                shadowDrawable.setBounds(view.left, view.top, view.right, view.bottom)
                shadowDrawable.draw(it)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopShadow()
    }

    fun stopShadow(actionStop: (() -> Unit)? = null) {
        shadowDrawable.stopAnimation()
        actionStop?.invoke()
    }

    fun startShadow(actionStart: (() -> Unit)? = null) {
        shadowDrawable.startAnimation()
        actionStart?.invoke()
    }
}
