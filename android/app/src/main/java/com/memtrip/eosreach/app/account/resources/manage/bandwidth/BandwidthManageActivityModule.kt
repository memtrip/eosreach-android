package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class BandwidthManageActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(BandwidthManageViewModel::class)
    internal abstract fun contributesManageBandwidthViewModel(viewModel: BandwidthManageViewModel): ViewModel
}