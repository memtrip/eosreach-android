package com.memtrip.eosreach.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.uikit_error_composite_view.view.*

class ErrorCompositeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.uikit_error_composite_view, this)
    }

    fun populate(title: String, body: String) {
        view_error_composite_title.text = title
        view_error_composite_body.text = body
    }

    fun retryClick(): Observable<Any> = RxView.clicks(view_error_composite_retry)
}