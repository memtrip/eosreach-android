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
package com.memtrip.eosreach.app.account.vote

import com.memtrip.eosreach.api.account.EosAccountVote
import com.memtrip.mxandroid.MxViewState

data class VoteViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class PopulateProxyVote(val proxyAccountName: String) : View()
        data class PopulateProducerVotes(val eosAccountVote: EosAccountVote) : View()
        object NoVoteCast : View()
        object NavigateToCastProducerVote : View()
        object NavigateToCastProxyVote : View()
        data class NavigateToViewProducer(val accountName: String) : View()
        data class NavigateToViewProxyVote(val accountName: String) : View()
        object OnVoteForUsProgress : View()
        object OnVoteForUsSuccess : View()
        data class OnVoteForUsError(val message: String, val log: String) : View()
    }
}