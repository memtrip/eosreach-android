package com.memtrip.eosreach.app.account.resources

import dagger.android.support.AndroidSupportInjection

class DefaultResourcesFragment : ResourcesFragment() {

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }
}