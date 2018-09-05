package com.memtrip.eosreach.app.settings.viewprivatekeys

import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.mxandroid.MxViewState

data class ViewPrivateKeysViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class ShowPrivateKeys(val privateKeys: List<EosPrivateKey>) : View()
        object OnProgress : View()
    }
}