package com.memtrip.eosreach.app.issue.createaccount

import com.memtrip.mxandroid.MxViewState

data class CreateAccountViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnCreateAccountProgress : View()
        data class OnCreateAccountSuccess(val privateKey: String) : View()
        data class CreateAccountError(val error: String) : View()
        object OnImportKeyProgress : View()
        data class ImportKeyError(val error: String) : View()
        object NavigateToAccountList : View()
    }
}