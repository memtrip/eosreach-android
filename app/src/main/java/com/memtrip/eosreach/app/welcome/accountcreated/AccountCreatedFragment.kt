package com.memtrip.eosreach.app.welcome.accountcreated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import javax.inject.Inject

internal class AccountCreatedFragment
    : MviFragment<AccountCreatedIntent, AccountCreatedRenderAction, AccountCreatedViewState, AccountCreatedViewLayout>(), AccountCreatedViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<AccountCreatedViewModel>

    @Inject
    lateinit var render: AccountCreatedViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.welcome_account_created_fragment, container, false)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<AccountCreatedIntent> = Observable.empty()

    override fun layout(): AccountCreatedViewLayout = this

    override fun model(): AccountCreatedViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountCreatedViewRenderer = render

    override fun showProgress() {

    }

    override fun showError() {

    }
}
