package com.memtrip.eosreach.app.account

interface AccountParentRefresh {
    fun triggerRefresh(page: AccountFragmentPagerAdapter.Page)
}