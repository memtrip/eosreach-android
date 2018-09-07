package com.memtrip.eosreach.app.account.vote

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class VoteFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(VoteViewModel::class)
    internal abstract fun contributesVoteViewModel(viewModel: VoteViewModel): ViewModel
}