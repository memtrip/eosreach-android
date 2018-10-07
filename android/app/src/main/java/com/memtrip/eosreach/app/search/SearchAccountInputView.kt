package com.memtrip.eosreach.app.search

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