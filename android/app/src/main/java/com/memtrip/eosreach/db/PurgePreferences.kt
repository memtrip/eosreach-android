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
package com.memtrip.eosreach.db

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.memtrip.eosreach.R
import javax.inject.Inject

class PurgePreferences @Inject internal constructor(
    application: Application
) {

    private val prefs: SharedPreferences = application.getSharedPreferences(
        application.getString(R.string.app_shared_preferences_package),
        Context.MODE_PRIVATE)

    private val keyPrefs: SharedPreferences = application.getSharedPreferences(
        application.getString(R.string.app_keys_shared_preferences_package),
        Context.MODE_PRIVATE)

    fun purgeAll() {
        prefs.edit().clear().apply()
        keyPrefs.edit().clear().apply()
    }
}