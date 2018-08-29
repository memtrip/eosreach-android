package com.memtrip.eosreach.app.welcome.createaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.welcome_create_account_fragment.*
import javax.inject.Inject

internal class CreateAccountFragment
    : MviFragment<CreateAccountIntent, CreateAccountRenderAction, CreateAccountViewState, CreateAccountViewLayout>(), CreateAccountViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CreateAccountViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.welcome_create_account_fragment, container, false)
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<CreateAccountIntent> {
        return RxView.clicks(welcome_create_account_create_button).map {
            CreateAccountIntent.CreateAccount(welcome_create_account_wallet_name_input.text.toString())
        }
    }

    override fun layout(): CreateAccountViewLayout = this

    override fun model(): CreateAccountViewModel = getViewModel(viewModelFactory)

    override fun render(): CreateAccountViewRenderer = render

    override fun showProgress() {
    }

    override fun success() {
        NavHostFragment.findNavController(this).navigate(
            R.id.welcome_navigation_action_createAccount_to_accountsList)
    }

    override fun showError() {
    }
}