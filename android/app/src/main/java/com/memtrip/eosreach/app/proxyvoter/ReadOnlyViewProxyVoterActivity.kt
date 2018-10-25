package com.memtrip.eosreach.app.proxyvoter

import android.content.res.Resources
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.proxyvoter.ProxyVoterDetails
import com.memtrip.eosreach.uikit.gone
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.proxy_voter_view_activity.*

class ReadOnlyViewProxyVoterActivity : ViewProxyVoterActivity() {

    override fun populate(proxyVoterDetails: ProxyVoterDetails) {
        super.populate(proxyVoterDetails)
        proxy_voter_view_owner_account_button.gone()
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(R.style.ReadOnlyAppTheme, true)
        return theme
    }
}