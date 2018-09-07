package com.memtrip.eosreach.app.account.vote

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class CastVoteActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(CastVoteViewModel::class)
    internal abstract fun contributesVoteViewModel(viewModel: CastVoteViewModel): ViewModel
}