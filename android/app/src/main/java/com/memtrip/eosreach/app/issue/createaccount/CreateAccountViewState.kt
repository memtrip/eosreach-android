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
package com.memtrip.eosreach.app.issue.createaccount

import com.android.billingclient.api.SkuDetails
import com.memtrip.mxandroid.MxViewState
import com.memtrip.mxandroid.MxViewState.Companion.id

data class CreateAccountViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object StartBillingConnection : View()
        data class OnSkuSuccess(val skuDetails: SkuDetails) : View()
        data class OnGetSkuError(val message: String) : View()
        data class OnAccountNameValidationPassed(val unique: Int = id()) : View()
        object OnCreateAccountLimbo : View()
        object OnCreateAccountLimboProgress : View()
        object OnCreateAccountProgress : View()
        data class OnCreateAccountSuccess(val purchaseToken: String, val privateKey: String) : View()
        data class CreateAccountError(val error: String, val unique: Int = id()) : View()
        object OnImportKeyProgress : View()
        data class ImportKeyError(val error: String) : View()
        object NavigateToAccountList : View()
    }
}