package com.memtrip.eosreach.app.account

import android.os.Bundle

import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity

import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.AndroidInjection

import io.reactivex.Observable

import javax.inject.Inject

class AccountActivity
    : MviActivity<AccountIntent, AccountRenderAction, AccountViewState, AccountViewLayout>(), AccountViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: AccountViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<AccountIntent> = Observable.empty()

    override fun layout(): AccountViewLayout = this

    override fun model(): AccountViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountViewRenderer = render

    override fun showProgress() {
    }

    override fun showError() {
    }
}