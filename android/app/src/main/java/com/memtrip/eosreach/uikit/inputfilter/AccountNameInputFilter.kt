package com.memtrip.eosreach.uikit.inputfilter

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class AccountNameInputFilter : InputFilter {

    private val pattern = Pattern.compile("^[A-Za-z0-5]+\$")!!

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        val result = "${dest.subSequence(0, dstart)}$source${dest.subSequence(dend, dest.length)}"

        return if (!pattern.matcher(result).matches()) {
            dest.subSequence(dstart, dend)
        } else {
            null
        }
    }
}