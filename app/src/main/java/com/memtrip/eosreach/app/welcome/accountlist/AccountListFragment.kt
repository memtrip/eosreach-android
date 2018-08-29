package com.memtrip.eosreach.app.welcome.accountlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountActivityArgs
import com.memtrip.eosreach.db.AccountEntity
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.welcome_account_list_fragment.*
import kotlinx.android.synthetic.main.welcome_account_list_fragment.view.*
import javax.inject.Inject

internal class AccountListFragment
    : MviFragment<AccountListIntent, AccountListRenderAction, AccountListViewState, AccountListViewLayout>(), AccountListViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: AccountListViewRenderer

    private lateinit var adapter: AccountListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.welcome_account_list_fragment, container, false)

        val adapterInteraction: PublishSubject<Interaction<AccountEntity>> = PublishSubject.create()
        adapter = AccountListAdapter(context!!, adapterInteraction)
        view.welcome_account_list_recyclerview.adapter = adapter

        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<AccountListIntent> = Observable.merge(
        Observable.just(AccountListIntent.Init),
        adapter.interaction.map {
            AccountListIntent.AccountSelected(it.data.accountName)
        }
    )

    override fun layout(): AccountListViewLayout = this

    override fun model(): AccountListViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountListViewRenderer = render

    override fun showProgress() {
        welcome_account_list_progressbar.visible()
        welcome_account_list_recyclerview.gone()
    }

    override fun showError() {
    }

    override fun navigateToSplash() {
        NavHostFragment.findNavController(this).navigate(
            R.id.welcome_navigation_action_accountsList_to_splash)
    }

    override fun showAccounts(accounts: List<AccountEntity>) {
        welcome_account_list_progressbar.gone()
        welcome_account_list_recyclerview.visible()
        adapter.clear()
        adapter.populate(accounts)
    }

    override fun navigateToAccount(accountName: String) {
        model().publish(AccountListIntent.Idle)
        NavHostFragment.findNavController(this).navigate(
            R.id.welcome_navigation_action_accountsList_to_account,
            AccountActivityArgs.Builder(accountName).build().toBundle())
    }
}
