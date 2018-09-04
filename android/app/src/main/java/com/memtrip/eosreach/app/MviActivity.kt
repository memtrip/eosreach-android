package com.memtrip.eosreach.app

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewActivity

import com.memtrip.mxandroid.MxViewIntent
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewState

abstract class MviActivity<VI : MxViewIntent, RA : MxRenderAction, VS : MxViewState, VL : MxViewLayout>
    : MxViewActivity<VI, RA, VS, VL>() {

    fun hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(window.decorView.getWindowToken(), 0)
    }
}