package com.memtrip.eosreach.brand

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class ReachButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.ButtonPrimary
) : AppCompatButton(context, attrs, defStyleAttr) {

    override fun drawableStateChanged() {
        super.drawableStateChanged()

        if (isEnabled) {
            alpha = 1.0f
        } else {
            alpha = 0.4f
        }
    }
}