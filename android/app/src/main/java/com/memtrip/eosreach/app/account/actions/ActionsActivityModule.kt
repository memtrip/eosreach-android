package com.memtrip.eosreach.app.account.actions

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey
import com.memtrip.eosreach.app.account.AccountActivity

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

import dagger.multibindings.IntoMap

@Module
abstract class ActionsActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(ActionsViewModel::class)
    internal abstract fun contributesActionsViewModel(viewModel: ActionsViewModel): ViewModel

    @ContributesAndroidInjector
    internal abstract fun contributeActionsActivity(): ActionsActivity
}