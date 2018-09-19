package com.memtrip.eosreach.app.welcome

import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.mxandroid.MxViewState

data class EntryViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
        object OnRsaEncryptionFailed : View()
        object NavigateToSplash : View()
        data class NavigateToAccount(val accountEntity: AccountEntity) : View()
        object NavigateToAccountList : View()
    }
}