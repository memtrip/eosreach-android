package com.memtrip.eosreach.app.welcome.accountlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import javax.inject.Inject

internal class AccountListFragment
    : MviFragment<AccountListIntent, AccountListRenderAction, AccountListViewState, AccountListViewLayout>(), AccountListViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: AccountListViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.welcome_accounts_list_fragment, container, false)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<AccountListIntent> = Observable.just(AccountListIntent.Init)

    override fun layout(): AccountListViewLayout = this

    override fun model(): AccountListViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountListViewRenderer = render

    override fun showProgress() {
    }

    override fun showError() {
    }

    override fun navigateToSplash() {
        NavHostFragment.findNavController(this).navigate(
            R.id.welcome_navigation_action_accountsListFragment_to_splashFragment)
    }

    override fun showAccounts() {
    }
}
