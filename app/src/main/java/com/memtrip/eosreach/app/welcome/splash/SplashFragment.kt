package com.memtrip.eosreach.app.welcome.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.jakewharton.rxbinding2.view.RxView

import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory

import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.welcome_splash_fragment.*
import javax.inject.Inject

internal class SplashFragment
    : MviFragment<SplashIntent, SplashRenderAction, SplashViewState, SplashViewLayout>(), SplashViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: SplashViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.welcome_splash_fragment, container, false)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
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
        findNavController(this).navigate(
            R.id.welcome_navigation_create_account,
            null,
            NavOptions.Builder().build())
    }

    override fun navigateToImportKey() {
        model().publish(SplashIntent.Init)
        findNavController(this).navigate(
            R.id.welcome_navigation_import_key,
            null,
            NavOptions.Builder().build())
    }
}
