package com.memtrip.eosreach.app.manage

import com.memtrip.eosreach.app.issue.importkey.ImportKeyFragment
import dagger.android.support.AndroidSupportInjection

class ManageImportKeyFragment : ImportKeyFragment() {

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun success() {
        // finish activity and load account picker
    }

    companion object {
        fun newInstance(): ManageImportKeyFragment = ManageImportKeyFragment()
    }
}