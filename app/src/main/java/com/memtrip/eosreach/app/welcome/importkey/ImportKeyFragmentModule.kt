package com.memtrip.eosreach.app.welcome.importkey

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ImportKeyFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributesImportKeyFragment(): ImportKeyFragment
}