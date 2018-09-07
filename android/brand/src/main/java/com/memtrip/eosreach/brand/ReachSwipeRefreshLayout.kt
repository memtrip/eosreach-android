package com.memtrip.eosreach.brand

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ReachSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    init {
        setColorSchemeColors(
            ContextCompat.getColor(context, R.color.colorAccent),
            ContextCompat.getColor(context, R.color.colorSecondary)
        )
    }
}