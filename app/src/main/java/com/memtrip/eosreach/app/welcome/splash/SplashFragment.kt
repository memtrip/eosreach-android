package com.memtrip.eosreach.app.welcome.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import javax.inject.Inject

internal class SplashFragment
    : MviFragment<SplashIntent, SplashRenderAction, SplashViewState, SplashViewLayout>(), SplashViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SplashViewModel>

    @Inject
    lateinit var render: SplashViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.welcome_splash_fragment, container, false)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<SplashIntent> = Observable.empty()

    override fun layout(): SplashViewLayout = this

    override fun model(): SplashViewModel = getViewModel(viewModelFactory)

    override fun render(): SplashViewRenderer = render

    override fun showProgress() {

    }

    override fun showError() {

    }
}
