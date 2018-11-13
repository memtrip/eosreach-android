/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.app.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountBundle
import com.memtrip.eosreach.app.account.ReadonlyAccountActivity.Companion.accountReadOnlyIntent
import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.explore_search_fragment.*
import kotlinx.android.synthetic.main.uikit_search_input_view.*
import kotlinx.android.synthetic.main.uikit_search_input_view.view.*
import javax.inject.Inject

class SearchFragment
    : MviFragment<SearchIntent, SearchRenderAction, SearchViewState, SearchViewLayout>(), SearchViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: SearchViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.explore_search_fragment, container, false)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<SearchIntent> = Observable.mergeArray(
        Observable.just(SearchIntent.Init),
        RxTextView.afterTextChangeEvents(explore_search_input_view.uikit_search_input_view_edittext).map { event ->
            val value = if (event.editable().isNullOrEmpty()) { "" } else {
                event.editable().toString()
            }
            SearchIntent.SearchValueChanged(value)
        },
        explore_search_input_account_error.retryClick().map {
            val value = if (uikit_search_input_view_edittext.text.isNullOrEmpty()) { "" } else {
                uikit_search_input_view_edittext.text.toString()
            }
            SearchIntent.SearchValueChanged(value)
        },
        explore_search_input_account_item_view.clicks().map {
            SearchIntent.ViewAccount(explore_search_input_account_item_view.accountEntity)
        }
    )

    override fun layout(): SearchViewLayout = this

    override fun model(): SearchViewModel = getViewModel(viewModelFactory)

    override fun render(): SearchViewRenderer = render

    override fun showProgress() {
        explore_search_progress.visible()
        explore_search_input_account_error.gone()
        explore_search_instruction_label.gone()
        explore_search_input_account_item_view.gone()
    }

    override fun showError() {
        explore_search_progress.gone()
        explore_search_input_account_error.visible()
        explore_search_input_account_error.populate(
            getString(R.string.explore_search_error_title),
            getString(R.string.explore_search_error_body)
        )
    }

    override fun searchResult(accountEntity: AccountEntity) {
        explore_search_progress.gone()
        explore_search_input_account_item_view.visible()
        explore_search_input_account_item_view.populate(accountEntity)
    }

    override fun viewAccount(accountEntity: AccountEntity) {
        model().publish(SearchIntent.Idle)
        startActivity(accountReadOnlyIntent(AccountBundle(accountEntity.accountName), context!!))
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}
