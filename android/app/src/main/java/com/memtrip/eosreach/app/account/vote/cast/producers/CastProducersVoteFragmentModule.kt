package com.memtrip.eosreach.app.account.vote.cast.producers

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class CastProducersVoteFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(CastProducersVoteViewModel::class)
    internal abstract fun contributesCastProducersVoteViewModel(viewModel: CastProducersVoteViewModel): ViewModel
}