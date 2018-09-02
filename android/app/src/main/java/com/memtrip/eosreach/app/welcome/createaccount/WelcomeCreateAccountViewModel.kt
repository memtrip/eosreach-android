package com.memtrip.eosreach.app.welcome.createaccount

import android.app.Application
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountViewModel
import javax.inject.Inject

class WelcomeCreateAccountViewModel @Inject internal constructor(
    application: Application
) : CreateAccountViewModel(application)