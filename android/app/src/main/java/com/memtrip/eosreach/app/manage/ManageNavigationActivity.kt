package com.memtrip.eosreach.app.manage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.NavigationActivity

class ManageNavigationActivity : NavigationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_navigation_activity)

        with (supportFragmentManager.beginTransaction()) {
            replace(R.id.manage_navigation_fragment, getFragment(screenExtra(intent)))
            commit()
        }
    }

    private fun getFragment(screen: Screen): Fragment = when (screen) {
        ManageNavigationActivity.Screen.CREATE_ACCOUNT -> ManageCreateAccountFragment.newInstance()
        ManageNavigationActivity.Screen.IMPORT_KEY -> ManageImportKeyFragment.newInstance()
    }

    enum class Screen {
        CREATE_ACCOUNT,
        IMPORT_KEY
    }

    companion object {

        private const val SCREEN_EXTRA: String = "SCREEN_EXTRA"

        fun manageNavigationIntent(screen: Screen, context: Context): Intent {
            return with (Intent(context, ManageNavigationActivity::class.java)) {
                putExtra(SCREEN_EXTRA, screen)
                this
            }
        }

        fun screenExtra(intent: Intent): Screen {
            return intent.getSerializableExtra(SCREEN_EXTRA) as Screen
        }
    }
}