package com.memtrip.eosreach.app.issue.createaccount

import com.android.billingclient.api.SkuDetails
import com.memtrip.mxandroid.MxViewState
import com.memtrip.mxandroid.MxViewState.Companion.id

data class CreateAccountViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnSkuProgress : View()
        data class OnSkuSuccess(val skuDetails: SkuDetails) : View()
        data class OnGetSkuError(val message: String) : View()
        data class OnAccountNameValidationPassed(val unique: Int = id()) : View()
        object OnCreateAccountProgress : View()
        data class OnCreateAccountSuccess(val purchaseToken: String, val privateKey: String) : View()
        data class CreateAccountError(val error: String, val unique: Int = id()) : View()
        object OnImportKeyProgress : View()
        data class ImportKeyError(val error: String) : View()
        object NavigateToAccountList : View()
    }
}