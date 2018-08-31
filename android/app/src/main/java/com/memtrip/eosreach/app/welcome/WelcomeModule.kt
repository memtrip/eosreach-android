package com.memtrip.eosreach.app.welcome

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey
import com.memtrip.eosreach.app.welcome.createaccount.WelcomeCreateAccountActivity
import com.memtrip.eosreach.app.welcome.importkey.WelcomeImportKeyActivity
import com.memtrip.eosreach.app.welcome.splash.SplashActivity
import com.memtrip.eosreach.app.welcome.splash.SplashViewModel

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

import dagger.multibindings.IntoMap

@Module
abstract class WelcomeModule {

    @Binds
    @IntoMap
    @ViewModelKey(EntryViewModel::class)
    internal abstract fun contributesAccountListViewModel(viewModel: EntryViewModel): ViewModel

    @ContributesAndroidInjector
    internal abstract fun contributeEntryActivity(): EntryActivity

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun contributesSplashViewModel(viewModel: SplashViewModel): ViewModel

    @ContributesAndroidInjector
    internal abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    internal abstract fun contributeWelcomeCreateAccountActivity(): WelcomeCreateAccountActivity

    @ContributesAndroidInjector
    internal abstract fun contributeWelcomeImportKeyActivity(): WelcomeImportKeyActivity
}