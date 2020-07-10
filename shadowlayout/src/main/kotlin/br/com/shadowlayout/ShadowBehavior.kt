package br.com.shadowlayout

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import br.com.shadowlayout.drawable.ShadowLinearDrawable

interface ShadowBehavior {

    val shadowDrawable: ShadowLinearDrawable

    fun ViewGroup.onJoinToWindow() {
        setWillNotDraw(false)
        shadowDrawable.callback = this
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    fun matchDrawable(who: Drawable): Boolean {
        return who === shadowDrawable
    }

    fun ViewGroup.beginToDraw(canvas: Canvas?, children: Sequence<View>) {
        shadowDrawable.parentWidth = measuredWidth
        if(!shadowDrawable.isRunning()) return
        children.forEach { view ->
            shadowDrawable.setBounds(view.left, view.top, view.right, view.bottom)
        }
        canvas?.let {
            shadowDrawable.draw(it)
        }
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