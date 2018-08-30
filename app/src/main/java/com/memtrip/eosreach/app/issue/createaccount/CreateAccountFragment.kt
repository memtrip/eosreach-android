package com.memtrip.eosreach.app.issue.createaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.issue_create_account_fragment.*
import javax.inject.Inject

abstract class CreateAccountFragment
    : MviFragment<CreateAccountIntent, CreateAccountRenderAction, CreateAccountViewState, CreateAccountViewLayout>(), CreateAccountViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CreateAccountViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.issue_create_account_fragment, container, false)
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<CreateAccountIntent> {
        return RxView.clicks(issue_create_account_create_button).map {
            CreateAccountIntent.CreateAccount(issue_create_account_wallet_name_input.text.toString())
        }
    }

    override fun layout(): CreateAccountViewLayout = this

    override fun model(): CreateAccountViewModel = getViewModel(viewModelFactory)

    override fun render(): CreateAccountViewRenderer = render

    override fun showProgress() {
    }

    override fun showError() {
    }

    abstract override fun success()
}