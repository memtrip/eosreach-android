package com.memtrip.eosreach.app.accountlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.AndroidInjection

import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import javax.inject.Inject

import kotlinx.android.synthetic.main.account_list_activity.*

internal class AccountListActivity
    : MviActivity<AccountListIntent, AccountListRenderAction, AccountListViewState, AccountListViewLayout>(), AccountListViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: AccountListViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_list_activity)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<AccountListIntent> = Observable.empty()

    override fun layout(): AccountListViewLayout = this

    override fun model(): AccountListViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountListViewRenderer = render

    override fun showProgress() {

    }

    override fun showError() {

    }
}
