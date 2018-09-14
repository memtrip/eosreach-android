package com.memtrip.eosreach.app.account.resources.manage.bandwidth.form

import android.app.Application
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthFormViewModel
import javax.inject.Inject

class DelegateBandwidthFormViewModel @Inject internal constructor(
    application: Application
): BandwidthFormViewModel(application)