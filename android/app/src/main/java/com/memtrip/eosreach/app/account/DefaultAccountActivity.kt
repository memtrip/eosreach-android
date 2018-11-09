package com.memtrip.eosreach.app.account

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import androidx.core.view.GravityCompat
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.explore.ExploreActivity.Companion.exploreIntent
import com.memtrip.eosreach.app.explore.SearchFragment
import kotlinx.android.synthetic.main.account_activity.*

class DefaultAccountActivity : AccountActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.account_menu)
    }

    override fun theme(): AccountTheme {
        return AccountTheme.DEFAULT
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(R.style.AppTheme, true)
        return theme
    }

    override fun onHomeUp() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.account_menu, menu)

        menu.findItem(R.id.account_menu_search).setOnMenuItemClickListener {
            model().publish(AccountIntent.BalanceTabIdle)
            startActivity(exploreIntent(this))
            true
        }
        return true
    }

    companion object {

        fun accountDefaultIntent(
            accountBundle: AccountBundle,
            context: Context,
            page: AccountFragmentPagerAdapter.Page = AccountFragmentPagerAdapter.Page.BALANCE
        ): Intent {
            return with(Intent(context, DefaultAccountActivity::class.java)) {
                putExtra(AccountActivity.ACCOUNT_EXTRA, accountBundle)
                putExtra(PAGE_SELECTION, page)
                this
            }
        }
    }
}