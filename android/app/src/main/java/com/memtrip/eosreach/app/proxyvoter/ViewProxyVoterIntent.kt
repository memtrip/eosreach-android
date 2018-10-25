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
package com.memtrip.eosreach.app.proxyvoter

import com.memtrip.eosreach.api.proxyvoter.ProxyVoterDetails
import com.memtrip.mxandroid.MxViewIntent

sealed class ViewProxyVoterIntent : MxViewIntent {
    object Idle : ViewProxyVoterIntent()
    data class Retry(val proxyVoterName: String) : ViewProxyVoterIntent()
    data class InitWithDetails(val proxyVoterDetails: ProxyVoterDetails) : ViewProxyVoterIntent()
    data class InitWithName(val proxyVoterName: String) : ViewProxyVoterIntent()
    data class NavigateToUrl(val url: String) : ViewProxyVoterIntent()
}