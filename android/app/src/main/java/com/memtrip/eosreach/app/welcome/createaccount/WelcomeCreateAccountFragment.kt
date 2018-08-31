package com.memtrip.eosreach.app.welcome.createaccount

import androidx.navigation.fragment.NavHostFragment
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountFragment
import dagger.android.support.AndroidSupportInjection

class WelcomeCreateAccountFragment : CreateAccountFragment() {

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun success() {
        NavHostFragment.findNavController(this).navigate(
            R.id.welcome_navigation_action_createAccount_to_accountsList)
    }
}