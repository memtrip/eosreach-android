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
