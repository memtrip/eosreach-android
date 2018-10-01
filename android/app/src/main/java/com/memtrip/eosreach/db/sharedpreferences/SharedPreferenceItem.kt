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
package com.memtrip.eosreach.db.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.memtrip.eosreach.R

abstract class SharedPreferenceItem<T>(
    context: Context,
    internal val prefs: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_shared_preferences_package),
        Context.MODE_PRIVATE)
) {
    abstract val key: String
    abstract fun put(value: T)
    abstract fun get(): T

    fun exists(): Boolean = prefs.contains(key)

    fun clear(): Unit = prefs.edit().remove(key).apply()
}