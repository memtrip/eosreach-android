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
package com.memtrip.eosreach.app.welcome.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView

import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.welcome.createaccount.WelcomeCreateAccountActivity.Companion.welcomeCreateAccountIntent
import com.memtrip.eosreach.app.welcome.importkey.WelcomeImportKeyActivity.Companion.welcomeImportKeyIntent
import dagger.android.AndroidInjection

import io.reactivex.Observable
import kotlinx.android.synthetic.main.welcome_splash_activity.*
import javax.inject.Inject

class SplashActivity
    : MviActivity<SplashIntent, SplashRenderAction, SplashViewState, SplashViewLayout>(), SplashViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: SplashViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_splash_activity)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<SplashIntent> = Observable.merge(
        RxView.clicks(welcome_splash_create_account_button).map { SplashIntent.NavigateToCreateAccount },
        RxView.clicks(welcome_splash_import_private_key_button).map { SplashIntent.NavigateToImportKeys }
    )

    override fun layout(): SplashViewLayout = this

    override fun model(): SplashViewModel = getViewModel(viewModelFactory)

    override fun render(): SplashViewRenderer = render

    override fun navigateToCreateAccount() {
        model().publish(SplashIntent.Init)
        startActivity(welcomeCreateAccountIntent(this))
    }

    override fun navigateToImportKey() {
        model().publish(SplashIntent.Init)
        startActivity(welcomeImportKeyIntent(this))
    }

    companion object {
        fun splashIntent(context: Context): Intent = Intent(context, SplashActivity::class.java)
    }
}
