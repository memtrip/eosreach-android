package com.memtrip.eosreach.app.manage

import androidx.navigation.fragment.NavHostFragment
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.issue.importkey.ImportKeyFragment

class ManageImportKeyFragment : ImportKeyFragment() {

    override fun success() {
        // finish activity and load account picker
    }
}