package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.UnDelegateBandwidthFormFragment
import kotlinx.android.synthetic.main.bandwidth_undelegate_activity.*

class UndelegateBandwidthContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bandwidth_undelegate_activity)

        setSupportActionBar(bandwidth_undelegate_toolbar)
        supportActionBar!!.title = getString(R.string.resources_bandwidth_confirm_undelegate_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.bandwidth_undelegate_fragment_container,
            UnDelegateBandwidthFormFragment.newInstance(
                bandwidthFormBundle(intent),
                contractAccountBalanceExtra(intent))
        )
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        } else -> super.onOptionsItemSelected(item)
    }

    companion object {

        private const val BANDWIDTH_FORM_BUNDLE = "BANDWIDTH_FORM_BUNDLE"
        private const val CONTRACT_ACCOUNT_BALANCE = "CONTRACT_ACCOUNT_BALANCE"

        fun undelegateBandwidthContainerIntent(
            bandwidthFormBundle: BandwidthFormBundle,
            contractAccountBalance: ContractAccountBalance,
            context: Context
        ): Intent {
            return with(Intent(context, UndelegateBandwidthContainerActivity::class.java)) {
                putExtra(BANDWIDTH_FORM_BUNDLE, bandwidthFormBundle)
                putExtra(CONTRACT_ACCOUNT_BALANCE, contractAccountBalance)
                this
            }
        }

        private fun bandwidthFormBundle(intent: Intent): BandwidthFormBundle =
            intent.getParcelableExtra(BANDWIDTH_FORM_BUNDLE)

        private fun contractAccountBalanceExtra(intent: Intent): ContractAccountBalance =
            intent.getParcelableExtra(CONTRACT_ACCOUNT_BALANCE)
    }
}