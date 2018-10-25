package com.memtrip.eosreach.app.issue.createaccount

import java.lang.ref.WeakReference

class StartConnectionRunnable(
    model: CreateAccountViewModel,
    private val createAccountIntent: CreateAccountIntent,
    private val createAccountViewModel: WeakReference<CreateAccountViewModel> = WeakReference(model)
) : Runnable {

    override fun run() {
        createAccountViewModel.get()?.publish(createAccountIntent)
    }
}