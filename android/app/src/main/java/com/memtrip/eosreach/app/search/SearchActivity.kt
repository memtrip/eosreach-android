package com.memtrip.eosreach.app.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountBundle
import com.memtrip.eosreach.app.account.ReadonlyAccountActivity.Companion.accountReadOnlyIntent
import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.search_activity.*
import kotlinx.android.synthetic.main.uikit_search_input_view.*
import kotlinx.android.synthetic.main.uikit_search_input_view.view.*
import javax.inject.Inject

class SearchActivity
    : MviActivity<SearchIntent, SearchRenderAction, SearchViewState, SearchViewLayout>(), SearchViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: SearchViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        setSupportActionBar(search_toolbar)
        supportActionBar!!.title = getString(R.string.search_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<SearchIntent> = Observable.mergeArray(
        Observable.just(SearchIntent.Init),
        RxTextView.afterTextChangeEvents(search_input_view.uikit_search_input_view_edittext).map { event ->
            val value = if (event.editable().isNullOrEmpty()) { "" } else {
                event.editable().toString()
            }
            SearchIntent.SearchValueChanged(value)
        },
        RxView.clicks(search_input_account_error).map {
            val value = if (uikit_search_input_view_edittext.text.isNullOrEmpty()) { "" } else {
                uikit_search_input_view_edittext.text.toString()
            }
            SearchIntent.SearchValueChanged(value)
        },
        search_input_account_item_view.clicks().map {
            SearchIntent.ViewAccount(search_input_account_item_view.accountEntity)
        }
    )

    override fun layout(): SearchViewLayout = this

    override fun model(): SearchViewModel = getViewModel(viewModelFactory)

    override fun render(): SearchViewRenderer = render

    override fun showProgress() {
        search_progress.visible()
        search_input_account_error.gone()
        search_input_account_item_view.gone()
    }

    override fun showError() {
        search_progress.gone()
        search_input_account_error.visible()
        search_input_account_error.populate(
            getString(R.string.search_error_title),
            getString(R.string.search_error_body)
        )
    }

    override fun searchResult(accountEntity: AccountEntity) {
        search_progress.gone()
        search_input_account_item_view.visible()
        search_input_account_item_view.populate(accountEntity)
    }

    override fun viewAccount(accountEntity: AccountEntity) {
        model().publish(SearchIntent.Idle)
        startActivity(accountReadOnlyIntent(AccountBundle(accountEntity.accountName), this))
    }

    companion object {

        fun searchIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }
}
