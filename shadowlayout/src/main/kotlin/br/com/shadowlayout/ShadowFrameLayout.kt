package br.com.shadowlayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.children
import br.com.shadowlayout.drawable.ShadowLinearDrawable


class ShadowFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ShadowBehavior {

    override val shadowDrawable: ShadowLinearDrawable = ShadowLinearDrawable()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onJoinToWindow()
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || matchDrawable(who)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        beginToDraw(canvas, this.children)
    }


}