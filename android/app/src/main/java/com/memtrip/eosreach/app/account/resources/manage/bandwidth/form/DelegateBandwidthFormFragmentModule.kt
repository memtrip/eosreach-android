package com.memtrip.eosreach.app.account.resources.manage.bandwidth.form

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DelegateBandwidthFormFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(DelegateBandwidthFormViewModel::class)
    internal abstract fun contributesDelegateBandwidthFormViewModel(viewModel: DelegateBandwidthFormViewModel): ViewModel
}