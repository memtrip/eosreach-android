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
package com.memtrip.eosreach.app.settings.viewprivatekeys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_private_keys_activity.*
import javax.inject.Inject

class ViewPrivateKeysActivity
    : MviActivity<ViewPrivateKeysIntent, ViewPrivateKeysRenderAction, ViewPrivateKeysViewState, ViewPrivateKeysViewLayout>(), ViewPrivateKeysViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ViewPrivateKeysViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_private_keys_activity)

        setSupportActionBar(view_private_keys_toolbar)
        supportActionBar!!.title = getString(R.string.view_private_keys_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ViewPrivateKeysIntent> = Observable.merge(
        Observable.just(ViewPrivateKeysIntent.Init),
        RxView.clicks(view_private_keys_button).map {
            ViewPrivateKeysIntent.DecryptPrivateKeys
        }
    )

    override fun layout(): ViewPrivateKeysViewLayout = this

    override fun model(): ViewPrivateKeysViewModel = getViewModel(viewModelFactory)

    override fun render(): ViewPrivateKeysViewRenderer = render

    override fun showPrivateKeys(viewKeyPair: List<ViewKeyPair>) {
        model().publish(ViewPrivateKeysIntent.Init)

        val privateKeyMarginBottom = resources.getDimensionPixelOffset(R.dimen.padding_medium)
        view_private_keys_button.gone()
        view_private_keys_progressbar.gone()
        view_private_keys_data_scrollview.visible()
        viewKeyPair.forEach { key ->
            val privateKeyLayout = with (LayoutInflater.from(this).inflate(
                R.layout.view_private_keys_item_layout,
                null,
                false
            ) as ViewGroup) {
                findViewById<TextView>(R.id.view_private_keys_item_private)
                    .text = key.eosPrivateKey.toString()
                findViewById<TextView>(R.id.view_private_keys_item_public)
                    .text = key.eosPrivateKey.publicKey.toString()
                findViewById<TextView>(R.id.view_private_keys_item_accounts)
                    .text = with (key.associatedAccounts.joinToString()) {
                        if (key.associatedAccounts.size > 1) {
                            this.dropLast(0)
                        } else {
                            this
                        }
                    }
                this
            }

            view_private_keys_data_container.addView(privateKeyLayout)

            (privateKeyLayout.layoutParams as
                LinearLayout.LayoutParams).bottomMargin = privateKeyMarginBottom
        }
    }

    override fun showProgress() {
        view_private_keys_instructions.gone()
        view_private_keys_button.gone()
        view_private_keys_progressbar.visible()
    }

    override fun showNoPrivateKeys() {
        view_private_keys_empty.visible()
        view_private_keys_progressbar.gone()
    }

    companion object {

        fun viewPrivateKeysIntent(context: Context): Intent {
            return Intent(context, ViewPrivateKeysActivity::class.java)
        }
    }
}
