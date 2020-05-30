package br.com.shadowlayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import br.com.shadowlayout.drawable.ShadowDrawable


class ShadowConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val shadowDrawable = ShadowDrawable()

    init {
        setWillNotDraw(false)
        shadowDrawable.callback = this
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === shadowDrawable
    }

    override fun dispatchDraw(canvas: Canvas?) {
        // call block() here if you want to draw behind children
        super.dispatchDraw(canvas)
        // call block() here if you want to draw over children

        children.forEach { view ->
            canvas?.let {
                shadowDrawable.setBounds(view.left, view.top, view.right, view.bottom)
                shadowDrawable.draw(it)
            }
        }
    }
}