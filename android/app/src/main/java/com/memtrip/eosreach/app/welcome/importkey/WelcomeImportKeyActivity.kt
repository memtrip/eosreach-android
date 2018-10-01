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
package com.memtrip.eosreach.app.welcome.importkey

import android.content.Context
import android.content.Intent
import com.memtrip.eosreach.app.issue.importkey.ImportKeyActivity
import com.memtrip.eosreach.app.welcome.EntryActivity
import dagger.android.AndroidInjection

class WelcomeImportKeyActivity : ImportKeyActivity() {

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun success() {
        startActivity(EntryActivity.entryIntent(this))
        finish()
    }

    override fun showGithubViewSource(): Boolean = true

    companion object {

        fun welcomeImportKeyIntent(context: Context): Intent {
            return Intent(context, WelcomeImportKeyActivity::class.java)
        }
    }
}