package com.memtrip.eosreach.app.account.vote.cast.proxy

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class CastProxyVoteFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(CastProxyVoteViewModel::class)
    internal abstract fun contributesCastProxyVoteViewModel(viewModel: CastProxyVoteViewModel): ViewModel
}