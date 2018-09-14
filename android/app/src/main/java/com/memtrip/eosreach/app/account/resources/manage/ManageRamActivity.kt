package com.memtrip.eosreach.app.account.resources.manage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.app.ViewModelFactory

import dagger.android.AndroidInjection
import io.reactivex.Observable
import javax.inject.Inject

import kotlinx.android.synthetic.main.manage_ram_activity.*

class ManageRamActivity
    : MviActivity<ManageRamIntent, ManageRamRenderAction, ManageRamViewState, ManageRamViewLayout>(), ManageRamViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ManageRamViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_ram_activity)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ManageRamIntent> = Observable.empty()

    override fun layout(): ManageRamViewLayout = this

    override fun model(): ManageRamViewModel = getViewModel(viewModelFactory)

    override fun render(): ManageRamViewRenderer = render

    override fun showProgress() {

    }

    override fun showError() {

    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"

        fun manageRamIntent(eosAccount: EosAccount, context: Context): Intent {
            return with (Intent(context, ManageRamActivity::class.java)) {
                putExtra(EOS_ACCOUNT_EXTRA, eosAccount)
                this
            }
        }

        private fun fromIntent(intent: Intent): EosAccount {
            return intent.getParcelableExtra(EOS_ACCOUNT_EXTRA) as EosAccount
        }
    }
}
