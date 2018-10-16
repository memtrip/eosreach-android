package com.memtrip.eosreach.app.proxyvoter

import dagger.android.AndroidInjection

class DefaultViewProxyVoterActivity : ViewProxyVoterActivity() {

    override fun inject() {
        AndroidInjection.inject(this)
    }
}