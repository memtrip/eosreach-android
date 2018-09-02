package com.memtrip.eosreach.app.welcome.importkey

import android.app.Application
import com.memtrip.eosreach.app.issue.importkey.ImportKeyUseCase
import com.memtrip.eosreach.app.issue.importkey.ImportKeyViewModel
import javax.inject.Inject

class WelcomeImportKeyViewModel @Inject internal constructor(
    importKeyUseCase: ImportKeyUseCase,
    application: Application
) : ImportKeyViewModel(importKeyUseCase, application)