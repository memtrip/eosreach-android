package com.memtrip.eosreach.app.account

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import com.memtrip.eosreach.R

class ReadonlyAccountActivity : AccountActivity() {

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(R.style.ReadOnlyAppTheme, true)
        return theme
    }

    override fun theme(): AccountTheme {
        return AccountTheme.READ_ONLY
    }

    companion object {

        fun accountReadOnlyIntent(
            accountBundle: AccountBundle,
            context: Context,
            page: AccountFragmentPagerAdapter.Page = AccountFragmentPagerAdapter.Page.BALANCE
        ): Intent {
            return with(Intent(context, ReadonlyAccountActivity::class.java)) {
                putExtra(AccountActivity.ACCOUNT_EXTRA, accountBundle)
                putExtra(PAGE_SELECTION, page)
                this
            }
        }
    }
}