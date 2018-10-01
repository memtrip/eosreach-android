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
package com.memtrip.eosreach.app

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewActivity

import com.memtrip.mxandroid.MxViewIntent
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewState

abstract class MviActivity<VI : MxViewIntent, RA : MxRenderAction, VS : MxViewState, VL : MxViewLayout>
    : MxViewActivity<VI, RA, VS, VL>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
    }

    fun hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(window.decorView.getWindowToken(), 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onHomeUp()
            true
        }
        else ->
            super.onOptionsItemSelected(item)
    }

    open fun onHomeUp() {
        finish()
    }

    protected inline fun <reified T : ViewModel> getViewModel(viewModelFactory: ViewModelProvider.Factory): T =
        ViewModelProviders.of(this, viewModelFactory)[T::class.java]
}

typealias ParentActivity = MviActivity<*, *, *, *>