package com.memtrip.eosreach.app.issue.createaccount

import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity

import com.memtrip.eosreach.app.ViewModelFactory
import io.reactivex.Observable
import kotlinx.android.synthetic.main.issue_create_account_activity.*
import javax.inject.Inject

abstract class CreateAccountActivity
    : MviActivity<CreateAccountIntent, CreateAccountRenderAction, CreateAccountViewState, CreateAccountViewLayout>(), CreateAccountViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CreateAccountViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.issue_create_account_activity)
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

    abstract override fun inject()

    abstract override fun success()
}