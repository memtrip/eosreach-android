package com.memtrip.eosreach.app.welcome.importkey

import androidx.navigation.fragment.NavHostFragment
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.issue.importkey.ImportKeyFragment

class WelcomeImportKeyFragment : ImportKeyFragment() {

    override fun success() {
        NavHostFragment.findNavController(this).navigate(
            R.id.welcome_navigation_action_importKey_to_accountsList)
    }
}