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
package com.memtrip.eosreach.app.explore

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.memtrip.eosreach.R
import com.memtrip.eosreach.uikit.inputfilter.AccountNameInputFilter
import kotlinx.android.synthetic.main.uikit_search_input_view.view.*

class SearchAccountInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.uikit_search_input_view, this)

        uikit_search_input_view_edittext.filters = arrayOf(
            InputFilter { source, _, _, _, _, _ ->
                source.toString().toLowerCase()
            },
            AccountNameInputFilter(),
            InputFilter.LengthFilter(resources.getInteger(R.integer.app_account_name_length))
        )

        uikit_search_input_view_edittext.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
    }
}