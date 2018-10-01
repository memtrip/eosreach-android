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
package com.memtrip.eosreach.app.account.vote.cast.producers

import com.memtrip.mxandroid.MxViewState

data class CastProducersVoteViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        data class AddProducerField(val nextPosition: Int) : View()
        data class AddExistingProducers(val producers: List<String>) : View()
        data class RemoveProducerField(val position: Int) : View()
        data class OnError(
            val message: String,
            val log: String,
            val unique: Int = MxViewState.id()
        ) : View()
        object OnSuccess : View()
        data class ViewLog(val log: String) : View()
    }
}