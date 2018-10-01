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
package com.memtrip.eosreach.uikit.inputfilter

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class AccountNameInputFilter : InputFilter {

    private val pattern = Pattern.compile("^[A-Za-z1-5]+\$")!!

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        val result = "${dest.subSequence(0, dstart)}$source${dest.subSequence(dend, dest.length)}"

        return if (source.isEmpty()) {
            null
        } else if (!pattern.matcher(result).matches()) {
            dest.subSequence(dstart, dend)
        } else {
            null
        }
    }
}