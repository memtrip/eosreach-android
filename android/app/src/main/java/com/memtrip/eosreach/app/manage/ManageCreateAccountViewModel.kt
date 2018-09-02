package com.memtrip.eosreach.app.manage

import android.app.Application
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountViewModel
import javax.inject.Inject

class ManageCreateAccountViewModel @Inject internal constructor(
    application: Application
) : CreateAccountViewModel(application)