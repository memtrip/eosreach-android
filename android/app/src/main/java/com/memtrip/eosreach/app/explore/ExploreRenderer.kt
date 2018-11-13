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
package com.memtrip.eosreach.app.explore

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ExploreRenderAction : MxRenderAction {
    object SearchTabIdle : ExploreRenderAction()
    object BlockProducersTabIdle : ExploreRenderAction()
    object Populate : ExploreRenderAction()
}

interface ExploreViewLayout : MxViewLayout {
    fun populate(page: ExploreFragmentPagerFragment.Page)
}

class ExploreViewRenderer @Inject internal constructor() : MxViewRenderer<ExploreViewLayout, ExploreViewState> {
    override fun layout(layout: ExploreViewLayout, state: ExploreViewState): Unit = when (state.view) {
        ExploreViewState.View.Idle -> {
        }
        ExploreViewState.View.Populate -> {
            layout.populate(state.page)
        }
    }
}