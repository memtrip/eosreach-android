package com.memtrip.eosreach.app.account.resources.manage.manageram.form

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount

import com.memtrip.eosreach.app.account.resources.manage.manageram.RamCommitType
import com.memtrip.eosreach.app.account.resources.manage.manageram.RamFormFragment

class BuyRamFormFragment : RamFormFragment() {

    override fun buttonLabel(): String = context!!.getString(R.string.resources_manage_ram_form_buy_button)
    override val ramCommitType: RamCommitType = RamCommitType.BUY

    companion object {

        fun newInstance(eosAccount: EosAccount): BuyRamFormFragment {
            return with (BuyRamFormFragment()) {
                arguments = RamFormFragment.toBundle(eosAccount)
                this
            }
        }
    }
}