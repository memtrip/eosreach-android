package com.memtrip.eosreach.uikit

import android.os.Handler
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun SwipeRefreshLayout.stop() {
    isRefreshing = false
}

fun SwipeRefreshLayout.start() {
    Handler().post { isRefreshing = true }
}