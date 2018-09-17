package com.memtrip.eosreach.app.account.vote.cast

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CastVoteRenderAction : MxRenderAction {
    data class TabIdle(val page: CastVoteFragmentPagerFragment.Page) : CastVoteRenderAction()
    data class Populate(val eosAccount: EosAccount, val page: CastVoteFragmentPagerFragment.Page) : CastVoteRenderAction()
}

interface CastVoteViewLayout : MxViewLayout {
    fun populate(eosAccount: EosAccount, page: CastVoteFragmentPagerFragment.Page)
}

class CastVoteViewRenderer @Inject internal constructor() : MxViewRenderer<CastVoteViewLayout, CastVoteViewState> {
    override fun layout(layout: CastVoteViewLayout, state: CastVoteViewState): Unit = when (state.view) {
        CastVoteViewState.View.Idle -> {
        }
        is CastVoteViewState.View.Populate -> {
            layout.populate(state.view.eosAccount, state.page)
        }
    }
}