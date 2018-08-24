package com.memtrip.eosreach.app

import com.memtrip.mxandroid.MxRenderAction

import com.memtrip.mxandroid.MxViewFragment
import com.memtrip.mxandroid.MxViewIntent
import com.memtrip.mxandroid.MxViewLayout

import com.memtrip.mxandroid.MxViewState

abstract class MviFragment<VI : MxViewIntent, RA : MxRenderAction, VS : MxViewState, VL : MxViewLayout>
    : MxViewFragment<VI, RA, VS, VL>() {

}