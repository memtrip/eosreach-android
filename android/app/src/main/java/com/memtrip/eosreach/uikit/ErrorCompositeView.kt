/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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