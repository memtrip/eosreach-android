package ${packageName}

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class ${className}FragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(${className}ViewModel::class)
    internal abstract fun contributes${className}ViewModel(viewModel: ${className}ViewModel): ViewModel
}