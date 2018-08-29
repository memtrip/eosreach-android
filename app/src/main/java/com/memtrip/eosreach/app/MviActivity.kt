package com.memtrip.eosreach.app

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewActivity

import com.memtrip.mxandroid.MxViewIntent
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewState

abstract class MviActivity<VI : MxViewIntent, RA : MxRenderAction, VS : MxViewState, VL : MxViewLayout>
    : MxViewActivity<VI, RA, VS, VL>()