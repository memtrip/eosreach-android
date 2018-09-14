package com.memtrip.eosreach.app.account.resources.manage.bandwidth.form

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UnDelegateBandwidthFormFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(UnDelegateBandwidthFormViewModel::class)
    internal abstract fun contributesUnDelegateBandwidthFormViewModel(viewModel: UnDelegateBandwidthFormViewModel): ViewModel
}