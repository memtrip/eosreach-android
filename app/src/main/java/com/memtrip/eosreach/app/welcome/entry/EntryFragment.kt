package com.memtrip.eosreach.app.welcome.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory

import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.welcome_account_list_fragment.*
import javax.inject.Inject

internal class EntryFragment
    : MviFragment<EntryIntent, AccountListRenderAction, EntryViewState, AccountListViewLayout>(), AccountListViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: AccountListViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.welcome_account_list_fragment, container, false)
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<EntryIntent> = Observable.just(EntryIntent.Init)

    override fun layout(): AccountListViewLayout = this

    override fun model(): EntryViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountListViewRenderer = render

    override fun showProgress() {
        welcome_account_list_progressbar.visible()
    }

    override fun showError() {
        welcome_account_list_progressbar.gone()
    }

    override fun navigateToSplash() {
        welcome_account_list_progressbar.gone()
        NavHostFragment.findNavController(this).navigate(
            R.id.welcome_navigation_action_accountsList_to_splash)
    }

    override fun navigateToAccount() {
        welcome_account_list_progressbar.gone()
        NavHostFragment.findNavController(this).navigate(
            R.id.welcome_navigation_action_accountsList_to_account)
    }
}
