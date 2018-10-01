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
package com.memtrip.eosreach.app.transaction.log

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class TransactionLogRenderAction : MxRenderAction {
    object Idle : TransactionLogRenderAction()
    data class ShowLog(val log: String) : TransactionLogRenderAction()
}

interface TransactionLogViewLayout : MxViewLayout {
    fun showLog(log: String)
}

class TransactionLogViewRenderer @Inject internal constructor() : MxViewRenderer<TransactionLogViewLayout, TransactionLogViewState> {
    override fun layout(layout: TransactionLogViewLayout, state: TransactionLogViewState): Unit = when (state.view) {
        TransactionLogViewState.View.Idle -> {
        }
        is TransactionLogViewState.View.ShowLog -> {
            layout.showLog(state.view.log)
        }
    }
}